/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.tiers.YvsBaseTiers;

import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComClient extends AbstractEntity {

    public String[] champ;
    public Object[] val;
    public String nameQueri;

    public AYvsComClient() {
    }

    public AYvsComClient(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComClient> controle(YvsComClient entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getCodeClient() == null || entity.getCodeClient().trim().equals("")) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le code client");
            }
            if ((entity.getCategorieComptable() != null) ? entity.getCategorieComptable().getId() < 1 : false) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer la catégorie comptable");
            }
            if (entity.getTiers() == null) {
                return new ResultatAction<>(false, entity, 0L, "Les informations du tiers ne peuvent pas etre nulles");
            }
            if (entity.getTiers().getSociete() == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser la société du tiers");
            }
            if (entity.getDefaut()) {
                YvsComClient y = searchClientDefaut(entity, entity.getTiers().getSociete());
                if (y != null ? y.getId() > 0 : false) {

                }
            }
            if (entity.getTiers() != null ? entity.getTiers().getId() > 0 : false) {
                YvsBaseTiers tiers = (YvsBaseTiers) dao.loadOneByNameQueries("YvsBaseTiers.findById", new String[]{"id"}, new Object[]{entity.getTiers().getId()});
                if (tiers != null ? tiers.getId() > 0 : false) {
                    YvsComClient t = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findByCode", new String[]{"code", "societe"}, new Object[]{entity.getCodeClient(), tiers.getSociete()});
                    if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                        if (t.getNom_prenom().equals(entity.getNom_prenom())) {
                            return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                        } else {
                            return new ResultatAction<>(false, entity, 0L, "Vous avez déja crée un client avec ce code");
                        }
                    }
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComClient.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComClient> save(YvsComClient entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_client", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    if (entity.getTiers() != null ? entity.getTiers().getId() < 1 : true) {
                        IYvsBaseTiers i_tiers = (IYvsBaseTiers) IEntitiSax.createInstance("IYvsBaseTiers", dao);
                        if (i_tiers == null) {
                            return new ResultatAction<>(false, entity, 0L, "Action Impossible");
                        }
                        ResultatAction tiers = i_tiers.save(entity.getTiers());
                        if (tiers != null ? !tiers.isResult() : true) {
                            return new ResultatAction<>(false, entity, 0L, tiers == null ? "Action Impossible" : tiers.getMessage());
                        }
                        entity.setTiers((YvsBaseTiers) tiers.getData());
                    }
                    entity.setId(null);
                    entity = (YvsComClient) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComClient.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComClient> update(YvsComClient entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                IYvsBaseTiers i_tiers = (IYvsBaseTiers) IEntitiSax.createInstance("IYvsBaseTiers", dao);
                if (i_tiers == null) {
                    return new ResultatAction<>(false, entity, 0L, "Action Impossible");
                }
                if (entity.getTiers() != null ? entity.getTiers().getId() > 0 : false) {
                    ResultatAction tiers = i_tiers.update(entity.getTiers());
                    if (tiers != null ? !tiers.isResult() : true) {
                        return new ResultatAction<>(false, entity, 0L, tiers == null ? "Action Impossible" : tiers.getMessage());
                    }
                }
                entity = (YvsComClient) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComClient.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComClient> delete(YvsComClient entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            IYvsBaseTiers i_tiers = (IYvsBaseTiers) IEntitiSax.createInstance("IYvsBaseTiers", dao);
            if (i_tiers == null) {
                return new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
            boolean succes = dao.delete(entity);
            if (succes) {
                if (entity.isDeleteTiers()) {
                    ResultatAction tiers = i_tiers.delete(entity.getTiers());
                    if (tiers != null ? !tiers.isResult() : true) {
                        return new ResultatAction<>(false, entity, 0L, "Le tiers n'a pas été supprimé");
                    }
                }
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public YvsComClient searchClientDefaut(YvsComClient bean, YvsSocietes currentScte) {
        boolean in = false;
        if (bean.getTiers().getVille() != null ? bean.getTiers().getVille().getId() > 0 : false) {
            champ = new String[]{"societe", "ville"};
            val = new Object[]{currentScte, new YvsDictionnaire(bean.getTiers().getVille().getId())};
            nameQueri = "YvsComClient.findDefautVille";
            in = true;
        } else {
            if (bean.getTiers().getPays() != null ? bean.getTiers().getPays().getId() > 0 : false) {
                champ = new String[]{"societe", "pays"};
                val = new Object[]{currentScte, new YvsDictionnaire(bean.getTiers().getPays().getId())};
                nameQueri = "YvsComClient.findDefautPays";
                in = true;
            }
        }
        if (in) {
            List<YvsComClient> lc = dao.loadNameQueries(nameQueri, champ, val);
            if (lc != null ? !lc.isEmpty() : false) {
                for (YvsComClient c : lc) {
                    if (!c.getId().equals(bean.getId())) {

                        return c;
                    }
                }
            }
        }

        return null;
    }

}
