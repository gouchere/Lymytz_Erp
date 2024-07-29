/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.param;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.commission.YvsComCommissionCommerciaux;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComCommissionCommerciaux extends AbstractEntity {

    public AYvsComCommissionCommerciaux() {
    }

    public AYvsComCommissionCommerciaux(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComCommissionCommerciaux> controle(YvsComCommissionCommerciaux entity) {
        try {
            if (entity.getIdDistant() < 1) {
                if (entity.getNumero() == null || entity.getNumero().trim().equals("")) {
                    return new ResultatAction<>(false, entity, 0L, "Vous devez entrer un numero!");
                }
            }
            if (entity.getMontant() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez preciser le montant!");
            }
            if (entity.getPeriode() != null ? entity.getPeriode().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez preciser la période!");
            }
            if (entity.getCommerciaux() != null ? entity.getCommerciaux().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez preciser le commercial!");
            }
            if (entity.getIdDistant() > 0) {
                YvsComCommissionCommerciaux t = (YvsComCommissionCommerciaux) dao.loadOneByNameQueries("YvsComCommissionCommerciaux.findOne", new String[]{"periode", "commerciaux"}, new Object[]{entity.getPeriode(), entity.getCommerciaux()});
                if (t != null ? t.getId() > 0 : false) {
                    return new ResultatAction<YvsComCommissionCommerciaux>(true, entity, 0L, "Succès", false);
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCommissionCommerciaux.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsComCommissionCommerciaux> save(YvsComCommissionCommerciaux entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_commission_commerciaux", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComCommissionCommerciaux) dao.loadOneByNameQueries("YvsComCommissionCommerciaux.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    if (entity.getIdDistant() > 0) {
                        String reference = dao.genererReference(Constantes.TYPE_COM_NAME, entity.getPeriode().getDateDebut(), 0, entity.getPeriode().getSociete(), currentAgence);
                        if (!asString(reference)) {
                            return new ResultatAction<>(false, entity, 0L, dao.getRESULT());
                        }
                        entity.setNumero(reference);
                    }
                    entity.setId(null);
                    entity = (YvsComCommissionCommerciaux) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCommissionCommerciaux.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsComCommissionCommerciaux> update(YvsComCommissionCommerciaux entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComCommissionCommerciaux) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCommissionCommerciaux.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComCommissionCommerciaux> delete(YvsComCommissionCommerciaux entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCommissionCommerciaux.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
