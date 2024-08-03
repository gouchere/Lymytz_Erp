/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComCategorieTarifaire;

import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComCategorieTarifaire extends AbstractEntity {

    public String[] champ;
    public Object[] val;
    public String nameQueri;

    public AYvsComCategorieTarifaire() {
    }

    public AYvsComCategorieTarifaire(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComCategorieTarifaire> controle(YvsComCategorieTarifaire entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getCategorie() != null) ? entity.getCategorie().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le client");

            }

            if ((entity.getClient() != null) ? entity.getClient().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier la catégorie");

            }
            if (entity.getPriorite() < 1) {
                return new ResultatAction<>(false, entity, 0L, "Vous precisez la priorité");
            }

            champ = new String[]{"client", "categorie"};
            val = new Object[]{entity.getClient(), new YvsBaseCategorieClient(entity.getCategorie().getId())};
            nameQueri = "YvsComCategorieTarifaire.findByClientCategorie";
            List<YvsComCategorieTarifaire> l = dao.loadNameQueries(nameQueri, champ, val);
            if (l != null ? !l.isEmpty() : false) {
                for (YvsComCategorieTarifaire c : l) {
                    if (!c.getId().equals(entity.getId())) {
                        return new ResultatAction<>(true, c, c.getId(), "Succes", false);

                    }
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComCategorieTarifaire.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComCategorieTarifaire> save(YvsComCategorieTarifaire entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_categorie_tarifaire", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComCategorieTarifaire) dao.loadOneByNameQueries("YvsComCategorieTarifaire.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComCategorieTarifaire) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCategorieTarifaire.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComCategorieTarifaire> update(YvsComCategorieTarifaire entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComCategorieTarifaire) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCategorieTarifaire.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComCategorieTarifaire> delete(YvsComCategorieTarifaire entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCategorieTarifaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
