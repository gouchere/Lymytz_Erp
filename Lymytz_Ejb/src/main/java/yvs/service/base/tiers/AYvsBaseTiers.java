/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.tiers.YvsBaseTiersTelephone;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseTiers extends AbstractEntity {

    public AYvsBaseTiers() {
    }

    public AYvsBaseTiers(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseTiers> controle(YvsBaseTiers entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getCodeTiers())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le code tiers");
            }
            if (!asString(entity.getNom())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le nom");
            }
            if (entity.getSociete() != null ? entity.getSociete().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser la société");
            }
            if (entity.getPays() != null ? entity.getPays().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le pays");
            }
            if (entity.getVille() != null ? entity.getVille().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer la ville");
            }
            YvsBaseTiers t = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findByCode", new String[]{"code", "societe"}, new Object[]{entity.getCodeTiers(), entity.getSociete()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                if (t.getNom_prenom().equals(entity.getNom_prenom())) {
                    return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Ce tiers existe déjà, veuillez changer de code");
                }
            }
            return new ResultatAction<>(true, entity, entity.getId(), "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseTiers.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseTiers> save(YvsBaseTiers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_tiers", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseTiers) dao.save1(entity);
                    try {
                        if (asString(entity.getTel()) ? entity.getTelephones().isEmpty() : false) {
                            YvsBaseTiersTelephone phone = new YvsBaseTiersTelephone(null, entity.getTel());
                            phone.setPrincipal(true);
                            phone.setTiers(entity);
                            dao.save1(phone);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(AYvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseTiers> update(YvsBaseTiers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseTiers) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseTiers> delete(YvsBaseTiers entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
