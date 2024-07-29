/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.utilisateur;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsUsers;

import yvs.service.AbstractEntity;
import yvs.service.base.produit.AYvsBaseArticleCodeBarre;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsUsers extends AbstractEntity {

    public AYvsUsers() {
    }

    public AYvsUsers(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsUsers> controle(YvsUsers entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getCodeUsers())) {
                return new ResultatAction<>(false, entity, 0L, "Le code user ne peut pas etre vide");
            }
            if (!asString(entity.getNom())) {
                return new ResultatAction<>(false, entity, 0L, "Le nom user ne peut pas etre vide");
            }
            if ((entity.getNiveauAcces() != null) ? entity.getNiveauAcces().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Le niveau d'acces ne peut pas etre null");
            }
            if ((entity.getAgence() != null) ? entity.getAgence().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "L'agence ne peut pas etre null");
            }
            YvsUsers y = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByCodeSociete", new String[]{"codeUsers", "societe"}, new Object[]{entity.getCodeUsers(), entity.getAgence().getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(entity.getId()) : false : false) {
                if (y.getNomUsers().equals(entity.getNomUsers())) {
                    return new ResultatAction<>(true, y, y.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez pas utiliser ce code, il est déja utilisé par un autre utilisateur");
                }
            }
            y = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByCode", new String[]{"codeUsers"}, new Object[]{entity.getCodeUsers()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(entity.getId()) : false : false) {
                return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez pas utiliser ce code, il est déja utilisé par un autre utilisateur");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsUsers> save(YvsUsers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_users", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsUsers) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    YvsNiveauUsers niveauUsers = (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findNivoUser", new String[]{"user", "niveau"}, new Object[]{entity, entity.getNiveauAcces()});
                    if (niveauUsers != null ? niveauUsers.getId() > 0 : false) {

                    } else if (entity.getNiveauAcces() != null) {
                        niveauUsers = new YvsNiveauUsers();
                        niveauUsers.setDateSave(new Date());
                        niveauUsers.setDateUpdate(new Date());
                        niveauUsers.setIdNiveau(entity.getNiveauAcces());
                        niveauUsers.setIdUser(entity);
                        niveauUsers = (YvsNiveauUsers) dao.save1(niveauUsers);
                        entity.getNiveauAcces().setIdNiveauUsers(niveauUsers.getId());
                    }
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsUsers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsUsers> update(YvsUsers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsUsers) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsUsers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsUsers> delete(YvsUsers entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
