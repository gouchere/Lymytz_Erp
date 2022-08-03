/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.param;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.Util;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.YvsSocietesAvis;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsSocietesAvis extends AbstractEntity {

    public AYvsSocietesAvis() {
    }

    public AYvsSocietesAvis(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsSocietesAvis> controle(YvsSocietesAvis entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getSociete() != null ? entity.getSociete().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Chosissez une socièté");
            }
            if (entity.getNote() < 1 && !Util.asString(entity.getCommentaire())) {
                return new ResultatAction<>(false, entity, 0L, "Entrer une note");
            }
            if (entity.getAuteur() != null ? entity.getAuteur().trim().isEmpty() : true) {
                return new ResultatAction<>(false, entity, 0L, "Entrer votre nom");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsSocietesAvis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsSocietesAvis> save(YvsSocietesAvis entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_societes_avis", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsSocietesAvis) dao.loadOneByNameQueries("YvsSocietesAvis.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsSocietesAvis) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsSocietesAvis.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsSocietesAvis> update(YvsSocietesAvis entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsSocietesAvis) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsSocietesAvis.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsSocietesAvis> delete(YvsSocietesAvis entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsSocietesAvis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
