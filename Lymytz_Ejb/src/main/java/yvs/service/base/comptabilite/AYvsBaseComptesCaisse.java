/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseComptesCaisse;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseComptesCaisse extends AbstractEntity {

    public AYvsBaseComptesCaisse() {
    }

    public AYvsBaseComptesCaisse(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseComptesCaisse> controle(YvsBaseComptesCaisse entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getCaisse() != null ? entity.getCaisse().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La caisse ne peut pas etre null");
            }

            if (entity.getCompteGeneral() != null ? entity.getCompteGeneral().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Le compte ne peut pas etre null");
            }
            YvsBaseComptesCaisse a = (YvsBaseComptesCaisse) dao.loadOneByNameQueries("YvsBaseComptesCaisse.findByCaisseCompte", new String[]{"caisse", "compte"}, new Object[]{entity.getCaisse(), entity.getCompteGeneral()});
            if (a != null ? (a.getId() > 0 ? !a.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, a, a.getId(), "Succès",false);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseComptesCaisse.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseComptesCaisse> save(YvsBaseComptesCaisse entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "Yvs_base_comptes_caisse", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseComptesCaisse) dao.loadOneByNameQueries("YvsBaseComptesCaisse.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseComptesCaisse) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseComptesCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseComptesCaisse> update(YvsBaseComptesCaisse entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseComptesCaisse) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseComptesCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseComptesCaisse> delete(YvsBaseComptesCaisse entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseComptesCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
