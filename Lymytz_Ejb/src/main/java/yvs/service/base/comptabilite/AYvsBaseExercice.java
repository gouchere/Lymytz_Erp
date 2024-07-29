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
import yvs.entity.base.YvsBaseExercice;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */


public class AYvsBaseExercice extends AbstractEntity {

    public AYvsBaseExercice() {
    } 

    public AYvsBaseExercice(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseExercice> controle(YvsBaseExercice entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getDateDebut() == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier la date de debut");

            }
            if (entity.getDateFin() == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier la date de fin");

            }
            if (entity.getReference() == null || entity.getReference().equals("")) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer la reference");

            }
            if (dateToCalendar(entity.getDateDebut()).after(dateToCalendar(entity.getDateFin()))) {
                return new ResultatAction<>(false, entity, 0L, "La date de fin doit preceder la date de debut");
            }
            if (entity.getDateDebut() == entity.getDateFin()) {
                return new ResultatAction<>(false, entity, 0L, "Veuillez entrer la durée d'une période de l'exercice");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseExercice.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseExercice> save(YvsBaseExercice entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_exercice", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseExercice) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseExercice.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseExercice> update(YvsBaseExercice entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseExercice) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseExercice.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseExercice> delete(YvsBaseExercice entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseExercice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
