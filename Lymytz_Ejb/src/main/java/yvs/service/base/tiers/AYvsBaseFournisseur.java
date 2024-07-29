/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseFournisseur extends AbstractEntity {

    public AYvsBaseFournisseur() {
    }

    public AYvsBaseFournisseur(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseFournisseur> controle(YvsBaseFournisseur entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getTiers() != null ? entity.getTiers().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le tiers");
            }
            if (entity.getCodeFsseur() == null || entity.getCodeFsseur().trim().equals("")) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le code fournisseur");
            }
            if ((entity.getCategorieComptable() != null) ? entity.getCategorieComptable().getId() < 1 : false) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer la catégorie comptable");
            }
            YvsBaseTiers tiers = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findById", new String[]{"id"}, new Object[]{entity.getTiers().getId()});
            if (tiers != null ? tiers.getId() > 0 : false) {
                YvsBaseFournisseur t = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findByCodeFsseur", new String[]{"codeFsseur", "societe"}, new Object[]{entity.getCodeFsseur(), tiers.getSociete()});
                if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                    if (t.getNom_prenom().equals(entity.getNom_prenom())) {
                        return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                    } else {
                        return new ResultatAction<>(false, entity, 0L, "Vous avez déja crée un fournisseur avec ce code");
                    }

                }
            }

            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseFournisseur.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsBaseFournisseur> save(YvsBaseFournisseur entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_fournisseur", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseFournisseur) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseFournisseur.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseFournisseur> update(YvsBaseFournisseur entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseFournisseur) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseFournisseur.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseFournisseur> delete(YvsBaseFournisseur entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseFournisseur.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
