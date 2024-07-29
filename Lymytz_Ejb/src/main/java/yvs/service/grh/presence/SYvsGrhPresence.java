/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.grh.presence;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.Util;
import yvs.entity.grh.param.YvsJoursFeries;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;
import yvs.entity.grh.presence.YvsGrhPointage;
import yvs.entity.grh.presence.YvsGrhPresence;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.service.base.param.IYvsGrhTrancheHoraire;
import yvs.service.grh.param.IYvsJoursOuvres;

/**
 *
 * @author Lymytz Dowes
 */
public class SYvsGrhPresence extends AYvsGrhPresence implements IYvsGrhPresence {

    @Override
    public YvsGrhPresence defined(YvsGrhEmployes employe, YvsGrhPlanningEmploye planning) {
        try {
            YvsGrhPresence y = new YvsGrhPresence();
            y.setHeureDebut(planning.getHeureDebut());
            y.setHeureFin(planning.getHeureFin());
            y.setHeureFinPrevu(planning.getHeureFin());
            y.setEmploye(employe);
            y.setDateDebut(planning.getDateDebut());
            y.setDateFin(planning.getDateFin());
            y.setDateFinPrevu(planning.getDateFin());
            y.setDureePause(planning.getDureePause());
            y.setValider(planning.isValide());
            y.setSupplementaire(planning.isSupplementaire());
            return y;
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPresence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public YvsGrhPresence getPresence(YvsGrhEmployes employe, YvsGrhPlanningEmploye planning) {
        try {
            if (employe != null ? employe.getId() > 0 : false) {
                if (planning != null ? planning.getId() > 0 : false) {
                    List<YvsGrhPresence> presences = dao.loadNameQueries("YvsGrhPresence.findByDatesAndEmploye", new String[]{"dateDebut", "dateFin", "employe"}, new Object[]{planning.getDateDebut(), planning.getDateFin(), employe});
                    if (presences != null ? presences.isEmpty() : true) {
                        presences = dao.loadNameQueries("YvsGrhPresence.findLastByDateDebutAndEmploye", new String[]{"dateDebut", "employe"}, new Object[]{planning.getDateDebut(), employe});
                    }
                    if (presences != null ? !presences.isEmpty() : false) {
                        YvsGrhPresence y = presences.get(0);
                        y.setDateDebut(dateTimeWithOutSecond(y.getDateDebut()));
                        y.setDateFin(dateTimeWithOutSecond(y.getDateFin()));
                        IYvsJoursOuvres i_jour = (IYvsJoursOuvres) IEntitiSax.createInstance("IYvsJoursOuvres", dao);
                        if (i_jour != null) {
                            YvsJoursOuvres jour = i_jour.getJour(employe, planning.getDateDebut());
                            if (jour != null ? jour.getId() > 0 : false) {
                                y.setSupplementaire(currentParamGrh.getLimitHeureSup() > 0 ? (!jour.getOuvrable() || jour.getJourDeRepos()) : false);
                            }
                        }
                        if (!y.isSupplementaire()) {
                            Date date = timestampToDate(y.getDateDebut());
                            YvsJoursFeries jour = (YvsJoursFeries) dao.loadOneByNameQueries("YvsJoursFeries.findByJour", new String[]{"societe", "jour"}, new Object[]{employe.getAgence().getSociete(), date});
                            if (jour != null ? jour.getId() > 0 : false) {
                                y.setSupplementaire(true);
                            }
                        }
                        return y;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPresence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public YvsGrhPresence getPresence(YvsGrhEmployes employe, Date current_time, boolean search_only) {
        try {
            if (employe != null ? employe.getId() > 0 : false) {
                if (currentParamGrh != null ? currentParamGrh.getId() < 1 : true) {
                    loadParametreGrh(employe.getAgence().getSociete());
                }
                current_time = dateTimeWithOutSecond(current_time);

                Date date_debut = timestampToDate(current_time);
                //On recherche la fiche de presence a la date debut et la date de fin
                List<YvsGrhPresence> presences = dao.loadNameQueries("YvsGrhPresence.findByDateAndEmploye", new String[]{"date", "employe"}, new Object[]{date_debut, employe});
                boolean sortie;//Defini la nature du mouvement (entree ou sortie)
                Date heure_fin;
                YvsGrhPointage last;
                IYvsGrhTrancheHoraire impl;
                YvsGrhTrancheHoraire tranche;
                for (YvsGrhPresence presence : presences) {
                    sortie = false;
                    last = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findLastEntreeByPresence", new String[]{"presence"}, new Object[]{presence});
                    if (last != null ? last.getId() > 0 : false) {
                        sortie = last.getHeureSortie() != null ? formatDateTime.format(last.getHeureSortie()).equals(NULL_DATETIME) : true;
                    }
                    heure_fin = datesToTimestamp(presence.getDateFinPrevu(), presence.getHeureFinPrevu());
                    //On Verifi si la date de pointage est egale à la date de début de la fiche
                    if (formatDate.format(presence.getDateDebut()).equals(formatDate.format(current_time))) {
                        if (!sortie) {//Si c'est uen entree
                            if (current_time.after(heure_fin)) {//On controle si le pointage est hors des marges de la fiche
                                // On modifie l'heure et/ou la date de fin prevu de la fiche
                                impl = (IYvsGrhTrancheHoraire) IEntitiSax.createInstance("IYvsGrhTrancheHoraire", dao);
                                if (impl != null) {
                                    tranche = impl.getTrancheHoraire(employe, current_time);
                                    if (tranche != null ? tranche.getId() > 0 : false) {
                                        presence.setHeureFinPrevu(tranche.getHeureFin());
                                        presence.setDateFinPrevu(definedTimestamp(datesToTimestamp(presence.getDateDebut(), presence.getHeureDebut()), presence.getHeureFinPrevu()));
                                        if (!search_only) {
                                            dao.update(presence);
                                        }
                                    }
                                }
                            }
                        }
                        presence.setHeureDebut(dateTimeWithOutSecond(datesToTimestamp(presence.getDateDebut(), presence.getHeureDebut())));
                        presence.setHeureFin(dateTimeWithOutSecond(datesToTimestamp(presence.getDateFin(), presence.getHeureFin())));
                        presence.setHeureFinPrevu(dateTimeWithOutSecond(datesToTimestamp(presence.getDateFinPrevu(), presence.getHeureFinPrevu())));
                        return presence;
                    } else {
                        if (sortie) {//Si c'est une sortie on ajoute la marge a l'heure de fin
                            Date timeMargeAvance = (currentParamGrh != null ? (currentParamGrh.getTimeMargeAvance() != null ? currentParamGrh.getTimeMargeAvance() : Util.getHeure(1, 0, 0)) : Util.getHeure(1, 0, 0));
                            heure_fin = addTimeToTimestamp(heure_fin, timeMargeAvance);
                        }
                        if (!current_time.after(heure_fin)) {//On controle si le pointage est dans les marges de la fiche
                            presence.setHeureDebut(dateTimeWithOutSecond(datesToTimestamp(presence.getDateDebut(), presence.getHeureDebut())));
                            presence.setHeureFin(dateTimeWithOutSecond(datesToTimestamp(presence.getDateFin(), presence.getHeureFin())));
                            presence.setHeureFinPrevu(dateTimeWithOutSecond(datesToTimestamp(presence.getDateFinPrevu(), presence.getHeureFinPrevu())));
                            return presence;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPresence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
