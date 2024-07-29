/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.users.YvsUsersAgence;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaCaissePieceDivers extends AbstractEntity {

    public AYvsComptaCaissePieceDivers() {
    }

    public AYvsComptaCaissePieceDivers(DaoInterfaceWs dao) {
        this.dao = dao;
    }
    public static DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");

    public ResultatAction<YvsComptaCaissePieceDivers> controle(YvsComptaCaissePieceDivers entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getDocDivers() != null ? entity.getDocDivers().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Veuillez selectionner un document");
            }
            if (entity.getStatutPiece().equals(Constantes.ETAT_REGLE)) {
                if (!entity.getDocDivers().getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                    return new ResultatAction<>(false, entity, 0L, "Le document doit être valider");
                }
            }

            if (entity.getMontant() == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le montant !");
            }

            if (entity.getBonProvisoire() != null ? entity.getBonProvisoire().getId() > 0 : false) {
                if (entity.isJustificatif() ? !entity.getBonProvisoire().getId().equals(entity.getJustify().getBon().getId()) : true) {
                    if (entity.getBonProvisoire().getReste() < entity.getMontant()) {
                        return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez pas justifier ce montant sur ce bon provisoire");

                    }
                }
            }
            YvsUsersAgence author = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{entity.getAuthor().getId()});
            System.err.println("author = " + author.getId());
            YvsBaseExercice y = giveExerciceActif(entity.getDatePiece(), author);
            System.err.println("exercice = " + y.getId());
            if (y != null ? y.getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucun exercice trouvé pour l'opération du " + formatDate.format(entity.getDatePiece()));
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaCaissePieceDivers.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaCaissePieceDivers> save(YvsComptaCaissePieceDivers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.setId(null);
                entity = (YvsComptaCaissePieceDivers) dao.save1(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceDivers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaCaissePieceDivers> update(YvsComptaCaissePieceDivers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaCaissePieceDivers) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceDivers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaCaissePieceDivers> delete(YvsComptaCaissePieceDivers entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public YvsBaseExercice giveExerciceActif(Date date, YvsUsersAgence currentUser) {
        if (date == null) {
            date = new Date();
        }
        YvsBaseExercice currentExo = new YvsBaseExercice();
        if (currentExo != null) {
            if ((currentExo.getDateDebut().before(date) || currentExo.getDateDebut().equals(giveOnlyDate(date)))
                    && (currentExo.getDateFin().after(date) || currentExo.getDateFin().equals(giveOnlyDate(date)))) {
                return currentExo;
            }
        }
        if (currentUser != null) {
            currentExo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentUser.getAgence().getSociete(), date});
            return currentExo;
        } else {
            return null;
        }

    }

    public static Date giveOnlyDate(Date begin) {
        Calendar d1 = Calendar.getInstance();
        if (begin != null) {
            d1.setTime(begin);
        }
        d1.set(Calendar.HOUR, 0);
        d1.set(Calendar.MINUTE, 0);
        d1.set(Calendar.SECOND, 0);
        d1.set(Calendar.MILLISECOND, 0);
        return d1.getTime();
    }
}
