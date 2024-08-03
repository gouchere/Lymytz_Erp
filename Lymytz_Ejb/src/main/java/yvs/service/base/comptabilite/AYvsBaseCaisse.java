/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseCaisse extends AbstractEntity {

    public AYvsBaseCaisse() {
    }

    public AYvsBaseCaisse(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseCaisse> controle(YvsBaseCaisse entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getCode() != null ? entity.getCode().trim().length() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser le code !");
            }
            if (entity.getIntitule() != null ? entity.getIntitule().trim().length() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser l'intitulé !");
            }
            if ((entity.getJournal() != null) ? entity.getJournal().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Veuillez indiquer un journal");
            }
            if (entity.getParent() != null) {
                if ((entity.getParent().getId() == entity.getId()) && entity.getId() > 0) {
                    return new ResultatAction<>(false, entity, 0L, "Erreur de référence cyclique entre les caisses !");
                }
            }
            if (entity.getDefaultCaisse()) {
                YvsBaseCaisse c = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findDefault", new String[]{"societe"}, new Object[]{entity.getJournal().getAgence().getSociete()});
                if (c != null ? (c.getId() != entity.getId()) : false) {
                    return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez définir une autre caisse par défaut");
                }
            }
            YvsBaseCaisse a = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findByCode", new String[]{"code"}, new Object[]{entity.getCode()});
            if (a != null ? (a.getId() > 0 ? !a.getId().equals(entity.getId()) : false) : false) {
                if (a.getIntitule().equals(entity.getIntitule())) {
                    return new ResultatAction<>(true, a, a.getId(), "Succès",false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Cette caisse existe déjà, veuillez entrer un autre code");
                }

            }
            return new ResultatAction<>(true, entity, entity.getId(), "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseCaisse.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseCaisse> save(YvsBaseCaisse entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_caisse", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseCaisse) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseCaisse> update(YvsBaseCaisse entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseCaisse) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseCaisse> delete(YvsBaseCaisse entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
