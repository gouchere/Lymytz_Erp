/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsNiveauAcces;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComEnteteDocVente extends AbstractEntity {

    public AYvsComEnteteDocVente() {
    }

    public AYvsComEnteteDocVente(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public AYvsComEnteteDocVente(DaoInterfaceLocal dao, YvsNiveauAcces niveau) {
        this.niveau = niveau;
        this.dao = dao;
    }

    public void setAgence(YvsAgences agence) {
        this.currentAgence = agence;
    }

    public void setNiveauAcces(YvsNiveauAcces niveau) {
        this.niveau = niveau;
    }

    public ResultatAction<YvsComEnteteDocVente> verifyDateVente(Date date, boolean update) {
        int ecart = -1;
        if (!update) {
            List<YvsComParametreVente> lp = dao.loadNameQueries("YvsComParametreVente.findByAgence",
                    new String[]{"agence"}, new Object[]{currentAgence}, 0, 1);
            if ((lp != null) ? !lp.isEmpty() : false) {
                ecart = lp.get(0).getJourAnterieur();
            }
        }
        return verifyDate(date, ecart);
    }

    public ResultatAction<YvsComEnteteDocVente> controle(YvsComEnteteDocVente entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getCreneau() != null) ? entity.getCreneau().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner un crenau !");
            }
            if ((entity.getCreneau().getCreneauPoint() != null) ? entity.getCreneau().getCreneauPoint().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez être planifié!");
            }
            if ((entity.getCreneau().getCreneauPoint().getPoint() != null) ? entity.getCreneau().getCreneauPoint().getPoint().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner un point de vente!");
            }
            if ((entity.getCreneau().getUsers() != null) ? entity.getCreneau().getUsers().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner le vendeur!");
            }
            if (entity.getDateEntete() == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner une date!");
            }
            if (entity.getId() > 1 ? !entity.isForce() : true) {
                YvsComEnteteDocVente y = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findOneFiche", new String[]{"date", "creno"}, new Object[]{entity.getDateEntete(), entity.getCreneau()});
                if (y != null ? y.getId() > 0 : false) {
                    return new ResultatAction<YvsComEnteteDocVente>(false, y, y.getId(), "Vous ne pouvez pas créer ou modifier ce journal de vente. Il existe déjà", false);
                }
            }
            //se rassurer que le user soit bien plannifié avec les infos 
            if (!entity.isSynchroniser()) {
                if (entity.getId() < 1 && autoriser("fv_save_only_pv", niveau)) {
                    List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"},
                            new Object[]{entity.getCreneau().getUsers(), Constantes.giveOnlyDate(entity.getDateEntete()), Constantes.givePrevOrNextDate(entity.getDateEntete(), -1)});
                    if (ids != null ? (ids.isEmpty() ? true : !ids.contains(entity.getCreneau().getCreneauPoint().getPoint().getId())) : true) {
                        return new ResultatAction<>(false, entity, 0L, "Ce vendeur n'est pas planifié au " + Constantes.dfN.format(entity.getDateEntete()));
                    }
                }
                ResultatAction re = verifyDateVente(entity.getDateEntete(), entity.getId() > 0);
                if (!re.isResult()) {
                    return re;
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComEnteteDocVente.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComEnteteDocVente> save(YvsComEnteteDocVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_entete_doc_vente", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    YvsComEnteteDocVente y = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findById", new String[]{"id"}, new Object[]{local});
                    if (y != null ? !y.getDateEntete().equals(entity.getDateEntete()) : true) {
                        entity.setId(null);
                        entity.setDateSave(new Date());
                        entity.setDateUpdate(new Date());
                        entity = (YvsComEnteteDocVente) dao.save1(entity);
                    } else {
                        entity = y;
                    }
                } else {
                    entity.setId(null);
                    entity.setDateSave(new Date());
                    entity.setDateUpdate(new Date());
                    entity = (YvsComEnteteDocVente) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succes", entity);
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComEnteteDocVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComEnteteDocVente> update(YvsComEnteteDocVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComEnteteDocVente) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComEnteteDocVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComEnteteDocVente> delete(YvsComEnteteDocVente entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComEnteteDocVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
