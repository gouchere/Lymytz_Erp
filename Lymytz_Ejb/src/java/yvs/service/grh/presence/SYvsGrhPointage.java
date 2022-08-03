/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.grh.presence;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.Util;
import yvs.entity.grh.param.YvsJoursFeries;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;
import yvs.entity.grh.presence.YvsGrhPointage;
import yvs.entity.grh.presence.YvsGrhPresence;
import yvs.entity.grh.presence.YvsPointeuse;
import yvs.entity.users.YvsUsersAgence;
import yvs.service.grh.param.IYvsJoursOuvres;

/**
 *
 * @author Lymytz Dowes
 */
public class SYvsGrhPointage extends AYvsGrhPointage implements IYvsGrhPointage {

    @Override
    public YvsGrhPointage defined(YvsGrhPresence presence, Date heure_entree, Date heure_sortie, YvsPointeuse pointeuse, boolean system_in, boolean system_out, YvsUsersAgence author) {
        try {
            return defined(presence, null, heure_entree, heure_sortie, false, pointeuse, system_in, system_out, author);
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPointage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public YvsGrhPointage defined(YvsGrhPresence presence, Date heure_entree, Date heure_sortie, boolean valider, YvsPointeuse pointeuse, boolean system_in, boolean system_out, YvsUsersAgence author) {
        try {
            return defined(presence, null, heure_entree, heure_sortie, valider, pointeuse, system_in, system_out, author);
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPointage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public YvsGrhPointage defined(YvsGrhPresence presence, YvsGrhPointage pointage, Date heure_entree, Date heure_sortie, boolean valider, YvsPointeuse pointeuse, boolean system_in, boolean system_out, YvsUsersAgence author) {
        try {
            if (pointage == null) {
                pointage = new YvsGrhPointage();
            }
            pointage.setPresence(presence);
            if (heure_entree != null) {
                pointage.setHeureEntree(heure_entree);
                pointage.setDateSaveEntree(heure_entree);
                pointage.setOperateurEntree(author.getUsers());
            }
            if (heure_sortie != null) {
                pointage.setHeureSortie(heure_sortie);
                pointage.setDateSaveSortie(heure_sortie);
                pointage.setOperateurSortie(author.getUsers());
            }
            if (!system_in ? heure_entree != null : false) {
                pointage.setHeurePointage(heure_entree);
            } else if (!system_out ? heure_sortie != null : false) {
                pointage.setHeurePointage(heure_sortie);
            }
            //!system_in && !system_out
            pointage.setHoraireNormale(!(system_in || system_out));
            pointage.setPointeuseIn(pointeuse);
            pointage.setPointeuseOut(pointeuse);
            pointage.setSystemIn(system_in);
            pointage.setSystemOut(system_out);
            pointage.setHeureSupplementaire(presence.isSupplementaire());
            pointage.setValider(!pointage.getHeureSupplementaire() ? valider : true);
            pointage.setCompensationHeure(false);
            pointage.setCommission(false);
            pointage.setPointageAutomatique(pointeuse != null);
            pointage.setDateUpdate(new Date());
            pointage.setActif(true);
            pointage.setAuthor(author);
            return pointage;
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPointage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean insertionPointage(YvsGrhEmployes employe, Date date, Date heure, YvsPointeuse pointeuse, int action, YvsUsersAgence author) {
        try {
            if (employe != null ? employe.getId() > 0 : false) {
                Date current_time = dateTimeWithOutSecond(datesToTimestamp(date, heure));
                YvsGrhPointage current = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findByHeureEmploye", new String[]{"employe", "heure"}, new Object[]{employe, current_time});
                if (current != null ? current.getId() < 1 : true) {
                    return onSavePointage(employe, current_time, date, pointeuse, action, author);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPointage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean onSavePointage(YvsGrhEmployes employe, Date current_time, Date current_date, YvsPointeuse pointeuse, int action, YvsUsersAgence author) {
        try {
            current_time = dateTimeWithOutSecond(current_time);
            if (employe != null ? employe.getId() > 0 : false) {
                if (currentParamGrh != null ? currentParamGrh.getId() < 1 : true) {
                    loadParametreGrh(employe.getAgence().getSociete());
                }
                YvsGrhPresence presence = null;
                IYvsGrhPresence i_presence = (IYvsGrhPresence) IEntitiSax.createInstance("IYvsGrhPresence", dao);
                if (i_presence != null) {
                    presence = i_presence.getPresence(employe, current_time, false);
                }
                if (presence != null ? presence.getId() < 1 : true) {//Si elle n'existe pas
                    //On recherche le planning en fonction de l'heure courante
                    YvsGrhPlanningEmploye planning = null;
                    IYvsGrhPlanningEmploye i_planning = (IYvsGrhPlanningEmploye) IEntitiSax.createInstance("IYvsGrhPlanningEmploye", dao);
                    if (i_planning != null) {
                        planning = i_planning.getPlanning(employe, current_time);
                    }
                    //On recherche la fiche de présence en fonction du planning
                    if (i_presence != null) {
                        presence = i_presence.getPresence(employe, planning);
                    }
                    if (presence != null ? presence.getId() < 1 : true) {
                        if (i_presence != null) {
                            presence = i_presence.defined(employe, planning);
                        }
                        if (presence != null) {
                            presence.setId(null);
                            presence.setAuthor(author);
                            presence = (YvsGrhPresence) dao.save1(presence);
                        }
                    }
                }
                if (presence != null ? presence.getId() < 1 : true) {
                    return onSavePointage(employe, current_time, current_date, pointeuse, action, author);
                } else {
                    IYvsJoursOuvres i_jour = (IYvsJoursOuvres) IEntitiSax.createInstance("IYvsJoursOuvres", dao);
                    if (i_jour != null) {
                        Date date = timestampToDate(presence.getDateDebut());
                        YvsJoursOuvres jour = i_jour.getJour(employe, date);
                        if (jour != null ? jour.getId() > 0 : false) {
                            presence.setSupplementaire(currentParamGrh.getLimitHeureSup() > 0 ? (!jour.getOuvrable() || jour.getJourDeRepos()) : false);
                        }
                    }
                }
                if (!presence.getValider()) {
                    if (!presence.isSupplementaire()) {
                        Date date = timestampToDate(presence.getDateDebut());
                        YvsJoursFeries jour = (YvsJoursFeries) dao.loadOneByNameQueries("YvsJoursFeries.findByJour", new String[]{"societe", "jour"}, new Object[]{employe.getAgence().getSociete(), date});
                        if (jour != null ? jour.getId() > 0 : false) {
                            presence.setSupplementaire(true);
                        }
                    }
                    return onSavePointage(presence, current_time, pointeuse, action, author);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPointage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean onSavePointage(YvsGrhPresence presence, Date current_time, YvsPointeuse pointeuse, int action, YvsUsersAgence author) {
        // action  ----  0—Check-In (default value) 1—Check-Out 2—Break-Out 3—Break-In 4—OT-In 5—OT-Out
        try {
            //Recherche le dernier pointage
            YvsGrhPointage last = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findLastEntreeByPresence", new String[]{"presence"}, new Object[]{presence});
            if (last != null ? last.getId() < 1 : true) {//S'il n'y'a pas de pointage
                //On insere une entrée
                switch (action) {
                    case 1:
                    case 2:
                    case 5:
                        return onSavePointage("S", null, presence, current_time, pointeuse, author);
                    case 3:
                    case 4:
                        return onSavePointage("E", null, presence, current_time, pointeuse, author);
                    default:
                        return onSavePointage("E", null, presence, current_time, pointeuse, author);
                }
            } else {
                //On verifi si le dernier pointage est une entrée
                if (last.getHeureSortie() != null ? formatDateTime.format(last.getHeureSortie()).equals(NULL_DATETIME) : true) {//Si le dernier pointage etait une entrée
                    //On insere une entrée
                    switch (action) {
                        case 1:
                        case 2:
                        case 5:
                            return onSavePointage("S", last, presence, current_time, pointeuse, author);
                        case 3:
                        case 4:
                            return onSavePointage("E", null, presence, current_time, pointeuse, author);
                        default:
                            return onSavePointage("S", last, presence, current_time, pointeuse, author);
                    }
                } else {//Si le dernier pointage etait une sortie
                    switch (action) {
                        case 1:
                        case 2:
                        case 5:
                            return onSavePointage("S", null, presence, current_time, pointeuse, author);
                        case 3:
                        case 4:
                            return onSavePointage("E", last, presence, current_time, pointeuse, author);
                        default:
                            return onSavePointage("E", last, presence, current_time, pointeuse, author);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPointage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean onSavePointage(String mouvement, YvsGrhPointage pointage, YvsGrhPresence presence, Date current_time, YvsPointeuse pointeuse, YvsUsersAgence author) {
        try {
            switch (mouvement) {
                case "S": {
                    if (pointage != null ? pointage.getId() > 0 : false) {
                        //On verifi si l'heure d'entrée etait inferieur a l'heure d'entree prevu
                        if (pointage.getHeureEntree().before(presence.getHeureDebut())) {//Si l'heure d'entree etait inferieur a l'heure d'entree prevu
                            //On verifi si l'heure actuelle est superieur a l'heure d'entree prevu
                            if (current_time.after(presence.getHeureDebut())) {//Si l'heure actuelle  est superieur a l'heure d'entree prevu
                                //On Complete la sortie du dernier pointage par l'heure d'entree prevu
                                YvsGrhPointage y = defined(presence, pointage, null, presence.getHeureDebut(), false, pointage.getPointeuseIn(), pointage.getSystemIn(), true, author);
                                dao.update(y);
                                //On verifi si l'heure actuelle est superieur a l'heure de sortie prevu
                                if (current_time.after(presence.getHeureFin())) { //Si l'heure actuelle est superieur a l'heure de sortie prevu
                                    //On insert un pointage supplementaire qui va de l'heure d'entre prevu a l'heure de sortie prevu
                                    y = defined(presence, presence.getHeureDebut(), presence.getHeureFin(), true, pointeuse, true, true, author);
                                    y = (YvsGrhPointage) dao.save1(y);
                                    if (y != null ? y.getId() > 0 : false) {
                                        //On insert un pointage supplementaire qui va de l'heure de sortie prevu a l'heure actuelle 
                                        y = defined(presence, presence.getHeureFin(), current_time, false, pointeuse, true, false, author);
                                        y = (YvsGrhPointage) dao.save1(y);
                                        return y != null ? y.getId() > 0 : false;
                                    }
                                } else {//Si l'heure actuelle est infereiur a l'heure de sortie prevu
                                    //On insert un pointage supplementaire qui va de l'heure d'entree prevu a l'heure actuelle
                                    y = defined(presence, presence.getHeureDebut(), current_time, true, pointeuse, true, false, author);
                                    y = (YvsGrhPointage) dao.save1(y);
                                    return y != null ? y.getId() > 0 : false;
                                }
                            } else {//Si l'heure actuelle est inferieur a l'heure d'entree prevu
                                YvsGrhPointage y = defined(presence, pointage, null, current_time, false, pointeuse, pointage.getSystemIn(), false, author);
                                dao.update(y);
                                return true;
                            }
                        } else if (!pointage.getHeureEntree().before(presence.getHeureFin())) {//Si l'heure d'entree etait superieur ou egale a l'heure de sortie prevu
                            //On Complete la sortie du dernier pointage par l'heure actuelle
                            YvsGrhPointage y = defined(presence, pointage, null, current_time, false, pointeuse, pointage.getSystemIn(), false, author);
                            dao.update(y);
                            return true;
                        } else {//Si l'heure d'entree etait compris entre l'heure d'entree prevu et l'heure de sortie prevu
                            //On verifi si l'heure actuelle est superieur a l'heure de sortie prevu
                            if (current_time.after(presence.getHeureFin())) {//Si l'heure actuelle est superieur a l'heure de sortie prevu
                                //On Complete la sortie du dernier pointage par l'heure de sortie prevu
                                YvsGrhPointage y = defined(presence, pointage, null, presence.getHeureFin(), true, pointage.getPointeuseIn(), pointage.getSystemIn(), true, author);
                                dao.update(y);
                                //On insert un pointage supplementaire qui va de l'heure de sortie prevu a l'heure actuelle
                                y = defined(presence, presence.getHeureFin(), current_time, false, pointeuse, true, false, author);
                                y = (YvsGrhPointage) dao.save1(y);
                                return y != null ? y.getId() > 0 : false;
                            } else {
                                //On Complete la sortie du dernier pointage par l'heure actuelle
                                YvsGrhPointage y = defined(presence, pointage, null, current_time, true, pointeuse, pointage.getSystemIn(), false, author);
                                dao.update(y);
                                return true;
                            }
                        }
                    } else {
                        //On verifi si l'heure actuelle est superieur a l'heure de sortie prevu
                        if (current_time.after(presence.getHeureFin())) {//Si l'heure actuelle est superieur a l'heure de sortie prevu
                            //On Complete la sortie du dernier pointage par l'heure de sortie prevu
                            YvsGrhPointage y = defined(presence, null, presence.getHeureFin(), false, pointeuse, false, true, author);
                            y = (YvsGrhPointage) dao.save1(y);
                            if (y != null ? y.getId() > 0 : false) {
                                //On insert un pointage supplementaire qui va de l'heure de sortie prevu a l'heure actuelle
                                y = defined(presence, presence.getHeureFin(), current_time, false, pointeuse, true, false, author);
                                y = (YvsGrhPointage) dao.save1(y);
                                return y != null ? y.getId() > 0 : false;
                            }
                        } else {
                            //On Complete la sortie du dernier pointage par l'heure actuelle
                            YvsGrhPointage y = defined(presence, null, current_time, false, pointeuse, false, false, author);
                            y = (YvsGrhPointage) dao.save1(y);
                            return y != null ? y.getId() > 0 : false;
                        }
                    }
                    break;
                }
                case "E": {
                    //On insert une entrée
                    Date timeMargeAvance = (currentParamGrh != null ? (currentParamGrh.getTimeMargeAvance() != null ? currentParamGrh.getTimeMargeAvance() : Util.getHeure(1, 0, 0)) : Util.getHeure(1, 0, 0));
                    Date debut = addTimeToTimestamp(presence.getHeureDebut(), timeMargeAvance);
                    if (presence.getHeureDebut().before(current_time) && current_time.before(debut)) {
                        presence.setMargeApprouve(timeMargeAvance);
//                        dao.update(presence);
                    } else {
                        presence.setMargeApprouve(timestampToDate(presence.getDateFin()));
//                        dao.update(presence);
                    }
                    YvsGrhPointage y = defined(presence, current_time, null, true, pointeuse, false, false, author);
                    y = (YvsGrhPointage) dao.save1(y);
                    return y != null ? y.getId() > 0 : false;
                }
                default:
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPointage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
