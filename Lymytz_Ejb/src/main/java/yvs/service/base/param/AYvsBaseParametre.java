/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsUsers;
import yvs.service.AbstractEntity;
import yvs.service.base.utilisateur.AYvsUsers;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseParametre extends AbstractEntity {

    String ERROR = "";

    public AYvsBaseParametre() {
    }

    public AYvsBaseParametre(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseParametre> controle(YvsBaseParametre entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
             System.out.println("mot de passe = " + entity.getDefautPassword());
            if (!entity.getGenererPassword() ? !correct(entity.getDefautPassword(), entity.getTaillePassword()) : false) {
                return new ResultatAction<>(false, entity, 0L, ERROR);
            }

            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseParametre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseParametre> save(YvsBaseParametre entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_parametre", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseParametre) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, result.getMessage());
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseParametre.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseParametre> update(YvsBaseParametre entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseParametre) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseParametre.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseParametre> delete(YvsBaseParametre entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseParametre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean correct(String password, int lenght) {

        if (!asString(password)) {
            ERROR = "Vous devez entrer un mot de passe";
            return false;
        }
        if (password.contains(" ")) {
            ERROR = "Le mot de passe ne doit pas contenir l'espace";
            return false;
        }
        if (password.trim().length() < lenght) {
            ERROR = "Le mot de passe doit etre sur " + lenght + " caractères";
            return false;
        }
        String regex = "ABCDEFGHIJKLPMNOQRSTUVWXYZ";
        boolean contains = false;
        for (int i = 0; i < password.trim().length(); i++) {
            if (regex.contains(password.charAt(i) + "")) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            ERROR = "Le mot de passe doit contenir au moins une(01) lettre majuscule";
            return false;
        }
        regex = "0123456789";
        contains = false;
        for (int i = 0; i < password.trim().length(); i++) {
            if (regex.contains(password.charAt(i) + "")) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            ERROR = "Le mot de passe doit contenir au moins un(01) chiffre";
            return false;
        }
        regex = "&~#'{([-|`_\\^@)]=}?/§!*+µ%$£¤<>.,;:²\"àéè";
        contains = false;
        for (int i = 0; i < password.trim().length(); i++) {
            if (regex.contains(password.charAt(i) + "")) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            ERROR = "Le mot de passe doit contenir au moins un(01) caractère spécial";
            return false;
        }
        return true;
    }
}
