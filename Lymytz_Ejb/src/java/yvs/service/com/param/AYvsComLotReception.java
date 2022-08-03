/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComLotReception extends AbstractEntity {

    public AYvsComLotReception() {
    }

    public AYvsComLotReception(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComLotReception> controle(YvsComLotReception entity) {
        try {
            if (entity.getNumero() == null || entity.getNumero().equals("")) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez preciser le numero");
            }
            YvsComLotReception t = (YvsComLotReception) dao.loadOneByNameQueries("YvsComLotReception.findByNumero", new String[]{"numero", "societe"}, new Object[]{entity.getNumero(), entity.getAgence().getSociete()});
            if (t != null ? t.getId() > 0 : false) {
                if (entity.getIdDistant() > 0) {
                    return new ResultatAction<YvsComLotReception>(true, entity, 0L, "Succès", false);
                } else {
                    if (!t.getId().equals(entity.getId())) {
                        return new ResultatAction<>(false, entity, 0L, "Vous avesz deja ajouté ce lot");
                    }
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsComLotReception.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsComLotReception> save(YvsComLotReception entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_lot_reception", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComLotReception) dao.loadOneByNameQueries("YvsComLotReception.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComLotReception) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComLotReception.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsComLotReception> update(YvsComLotReception entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComLotReception) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComLotReception.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComLotReception> delete(YvsComLotReception entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComLotReception.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public List<YvsComLotReception> loadList(long depot, long conditionnement, Date date, double quantite, double stock) {
        List<YvsComLotReception> list = new ArrayList<>();
        try {
            String query = "SELECT y.id, y.numero, y.date_fabrication, y.date_expiration, get_stock(m.article, 0, m.depot, 0, 0, ?::date, m.conditionnement, l.lot) "
                    + "FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_mouvement_stock_lot l ON m.id = l.mouvement INNER JOIN yvs_com_lot_reception y ON l.lot = y.id "
                    + "WHERE m.depot = ? AND m.conditionnement = ? GROUP BY y.id, m.article, m.depot, m.conditionnement, l.lot "
                    + "HAVING get_stock(m.article, 0, m.depot, 0, 0, ?::date, m.conditionnement, l.lot) > 0 ORDER BY y.date_expiration, y.date_fabrication, y.numero";
            Options[] params = new Options[]{new Options(date, 1), new Options(depot, 2), new Options(conditionnement, 3), new Options(date, 4)};
            List<Object[]> result = dao.loadListBySqlQuery(query, params);
            YvsComLotReception y;
            for (Object[] lect : result) {
                y = new YvsComLotReception((Long) lect[0], (String) lect[1], new Date(((java.sql.Date) lect[2]).getTime()), new Date(((java.sql.Date) lect[3]).getTime()), (Double) lect[4]);
                if (quantite > 0) {
                    if (quantite > y.getStock()) {
                        y.setQuantitee(y.getStock());
                        quantite -= y.getStock();
                    } else {
                        y.setQuantitee(quantite);
                        quantite = 0;
                    }
                }
                if (stock > 0) {
                    stock -= y.getStock();
                }
                list.add(y);
            }
            if (stock > 0) {
                YvsComLotReception no = new YvsComLotReception(-1L, "SANS LOT", new Date(), new Date(), stock);
                list.add(0, no);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
