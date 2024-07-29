/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.param;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.users.YvsUsers;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComCreneauHoraireUsers extends AbstractEntity {

    public AYvsComCreneauHoraireUsers() {
    }

    public AYvsComCreneauHoraireUsers(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComCreneauHoraireUsers> controle(YvsComCreneauHoraireUsers entity) {
        try {
            if ((entity.getUsers() != null) ? entity.getUsers().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner l'utilisateur");
            }
            if (((entity.getCreneauDepot() != null) ? entity.getCreneauDepot().getId() < 1 : true) && ((entity.getCreneauPoint() != null) ? entity.getCreneauPoint().getId() < 1 : true)) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le creneau");
            }
            if (entity.getCreneauDepot() != null ? entity.getCreneauDepot().getId() > 0 : false) {
                if (entity.getActif() && !entity.getCreneauDepot().getActif()) {
                    return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez pas créer ce créneau car son type est désactivé");
                }
            }
            if (entity.getCreneauPoint() != null ? entity.getCreneauPoint().getId() > 0 : false) {
                if (entity.getActif() && !entity.getCreneauPoint().getActif()) {
                    return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez pas créer ce créneau car son type est désactivé");
                }
            }
            if (!entity.getPermanent()) {
                champ = new String[]{"users", "dateTravail", "creneau"};
                val = new Object[]{new YvsUsers(entity.getUsers().getId()), entity.getDateTravail(), new YvsComCreneauDepot(entity.getCreneauDepot().getId())};
                nameQueri = "YvsComCreneauHoraireUsers.findByUsersDateCreneau";
                List<YvsComCreneauHoraireUsers> lc = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
                if (lc != null ? !lc.isEmpty() : false) {
                    if (!lc.get(0).getId().equals(entity.getId())) {
                        return new ResultatAction<YvsComCreneauHoraireUsers>(true, lc.get(0), lc.get(0).getId(), "Vous avez déja programmé ce planning", false);
                    }
                }
            } else {
                champ = new String[]{"users", "permanent", "creneau"};
                val = new Object[]{new YvsUsers(entity.getUsers().getId()), true, new YvsComCreneauDepot(entity.getCreneauDepot().getId())};
                nameQueri = "YvsComCreneauHoraireUsers.findByUsersPermanentCreneau";
                List<YvsComCreneauHoraireUsers> lc = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
                if (lc != null ? !lc.isEmpty() : false) {
                    if (!lc.get(0).getId().equals(entity.getId())) {
                        return new ResultatAction<YvsComCreneauHoraireUsers>(true, lc.get(0), lc.get(0).getId(), "Vous avez déja programmé ce planning", false);
                    }
                }

                champ = new String[]{"users", "depot"};
                val = new Object[]{new YvsUsers(entity.getUsers().getId()), new YvsBaseDepots(entity.getCreneauDepot().getDepot().getId())};
                nameQueri = "YvsComCreneauHoraireUsers.findByUsersPermActifDepot";
                lc = dao.loadNameQueries(nameQueri, champ, val);
                for (YvsComCreneauHoraireUsers c : lc) {
                    if (!c.getId().equals(entity.getId())) {
                        return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez pas activer ce créneau car cette employé a deja un créneau permanent pour ce depot");
                    }
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCreneauHoraireUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsComCreneauHoraireUsers> save(YvsComCreneauHoraireUsers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_creneau_horaire_users", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComCreneauHoraireUsers) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCreneauHoraireUsers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsComCreneauHoraireUsers> update(YvsComCreneauHoraireUsers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComCreneauHoraireUsers) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCreneauHoraireUsers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComCreneauHoraireUsers> delete(YvsComCreneauHoraireUsers entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCreneauHoraireUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
