/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.grh.presence;

import java.util.Calendar;
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
import static yvs.service.AbstractEntity.formatJour;
import yvs.service.base.param.IYvsGrhTrancheHoraire;
import yvs.service.grh.param.IYvsJoursOuvres;

/**
 *
 * @author Lymytz Dowes
 */
public class SYvsGrhPlanningEmploye extends AYvsGrhPlanningEmploye implements IYvsGrhPlanningEmploye {

    @Override
    public YvsGrhPlanningEmploye defaut(Date heure) {
        try {
            Calendar debut = dateToCalendar(heure);
            debut.set(Calendar.HOUR_OF_DAY, 7);
            debut.set(Calendar.MINUTE, 30);
            debut.set(Calendar.SECOND, 0);
            Calendar fin = dateToCalendar(heure);
            fin.set(Calendar.HOUR_OF_DAY, 17);
            fin.set(Calendar.MINUTE, 30);
            fin.set(Calendar.SECOND, 0);
            Calendar pause = dateToCalendar(heure);
            pause.set(Calendar.HOUR_OF_DAY, 1);
            pause.set(Calendar.MINUTE, 30);
            pause.set(Calendar.SECOND, 0);
            YvsGrhPlanningEmploye planning = new YvsGrhPlanningEmploye();
            planning.setId(1L);
            planning.setDateDebut(timestampToDate(heure));
            planning.setDateFin(timestampToDate(heure));
            planning.setHeureDebut(debut.getTime());
            planning.setHeureFin(fin.getTime());
            planning.setDureePause(pause.getTime());
            planning.setValide(false);
            return planning;
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPointage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public YvsGrhPlanningEmploye defined(YvsJoursOuvres jour, Date date) {
        try {
            YvsGrhPlanningEmploye y = new YvsGrhPlanningEmploye();
            if (jour != null) {
                y.setId(jour.getId());
                y.setDateDebut(date);
                y.setDateFin(definedTimestamp(datesToTimestamp(date, jour.getHeureDebutTravail()), jour.getHeureFinTravail()));
                y.setDureePause(jour.getDureePause());
                y.setHeureDebut(jour.getHeureDebutTravail());
                y.setHeureFin(jour.getHeureFinTravail());
                y.setSupplementaire(currentParamGrh.getLimitHeureSup() > 0 ? (!jour.getOuvrable() || jour.getJourDeRepos()) : false);
            }
            return y;
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPlanningEmploye.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public YvsGrhPlanningEmploye getSimplePlanning(YvsGrhEmployes employe, Date heure) {
        try {
            if (employe != null ? employe.getId() > 0 : false) {
                if (currentParamGrh != null ? currentParamGrh.getId() < 1 : true) {
                    loadParametreGrh(employe.getAgence().getSociete());
                }
                Date timeMargeAvance = (currentParamGrh != null ? (currentParamGrh.getTimeMargeAvance() != null ? currentParamGrh.getTimeMargeAvance() : Util.getHeure(1, 0, 0)) : Util.getHeure(1, 0, 0));
                YvsGrhPlanningEmploye planning = new YvsGrhPlanningEmploye();
                // On verifie si l'employé a un horaire dynamique
                if (employe.getHoraireDynamique()) { // Si oui
                    // On recherche le planning de l'employé a la date
                    Date date = timestampToDate(heure);
                    List<YvsGrhPlanningEmploye> plannings = dao.loadNameQueries("YvsGrhPlanningEmploye.findByDateAndEmploye", new String[]{"employe", "date"}, new Object[]{employe, date});
                    if (plannings != null ? !plannings.isEmpty() : false) {
                        if (plannings.size() > 1) {
                            Date date_debut, date_fin;
                            Date heure_debut, heure_fin;
                            List<YvsGrhPresence> presences;
                            YvsGrhPresence presence;
                            YvsGrhPointage last;
                            for (int i = 0; i < plannings.size(); i++) {
                                planning = plannings.get(i);
                                date_debut = timestampToDate(planning.getDateDebut());
                                date_fin = timestampToDate(planning.getDateFin());
                                heure_debut = datesToTimestamp(date_debut, planning.getHeureDebut());
                                heure_fin = datesToTimestamp(date_fin, planning.getHeureFin());

                                // On verifi si l'heure est compris dans le planning actuelle
                                if (!heure_debut.after(heure) && !heure.after(heure_fin)) { // Si l'heure est compris on sort
                                    break;
                                } else if (heure.before(heure_debut) && i == 0) { // Si l'heure est inferieur au 1er planning on sort
                                    break;
                                } else if (heure.after(heure_fin)) {
                                    // On verifie s'il a une fiche de présence a ce planning
                                    presences = dao.loadNameQueries("YvsGrhPresence.findLastByDateDebutAndEmploye", new String[]{"dateDebut", "employe"}, new Object[]{date_debut, employe});
                                    if (presences != null ? !presences.isEmpty() : false) {
                                        presence = presences.get(0);
                                        // On verifie s'il le dernier pointage de la fiche est une entrée                                        
                                        last = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findLastEntreeByPresence", new String[]{"presence"}, new Object[]{presence});
                                        if (last != null ? last.getId() > 0 : false) {
                                            if (last.getHeureSortie() != null ? formatDateTime.format(last.getHeureSortie()).equals(NULL_DATETIME) : true) {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            // On verifie si l'heure est dans le bon interval
                            if (planning != null ? planning.getId() > 0 : false) {
                                heure_fin = dateTimeWithOutSecond(datesToTimestamp(planning.getDateFin(), planning.getHeureFin()));
                                heure_fin = addTimeToTimestamp(heure_fin, timeMargeAvance);
                                if (heure.after(heure_fin)) {
                                    // On verifie s'il a une fiche de présence a ce planning
                                    presences = dao.loadNameQueries("YvsGrhPresence.findLastByDateDebutAndEmploye", new String[]{"dateDebut", "employe"}, new Object[]{planning.getDateDebut(), employe});
                                    if (presences != null ? !presences.isEmpty() : false) {
                                        presence = presences.get(0);
                                        // On verifie s'il le dernier pointage de la fiche est une entrée                                        
                                        last = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findLastEntreeByPresence", new String[]{"presence"}, new Object[]{presence});
                                        if (last != null ? last.getId() > 0 : false) {
                                            if (last.getHeureSortie() != null ? !formatDateTime.format(last.getHeureSortie()).equals(NULL_DATETIME) : false) {
                                                planning = null;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (verifyDateHeure(plannings.get(0), heure)) {
                                planning = plannings.get(0);
                            }
                        }
                    }
                    // On verifi si le jour est un jour ouvrable
                    if (planning != null ? planning.getId() > 0 : false) {
                        IYvsJoursOuvres i_jour = (IYvsJoursOuvres) IEntitiSax.createInstance("IYvsJoursOuvres", dao);
                        if (i_jour != null) {
                            YvsJoursOuvres jour = i_jour.getJour(employe, planning.getDateDebut());
                            if (jour != null ? jour.getId() > 0 : false) {
                                planning.setSupplementaire(currentParamGrh.getLimitHeureSup() > 0 ? (!jour.getOuvrable() || jour.getJourDeRepos()) : false);
                            }
                        }
                    }
                } else {
                    if (employe.getContrat() != null ? employe.getContrat().getId() > 0 : false) {
                        if (employe.getContrat().getCalendrier() != null ? employe.getContrat().getCalendrier().getId() > 0 : false) {
                            Calendar c = dateToCalendar(heure);
                            c.add(Calendar.DATE, -1);;
                            YvsJoursOuvres jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"calendrier", "jour"}, new Object[]{employe.getContrat().getCalendrier(), formatJour.format(c.getTime())});
                            if (jour != null ? jour.getId() > 0 : false) {
                                planning = defined(jour, c.getTime());
                            }
                            boolean deja = false;
                            if (planning != null ? planning.getId() > 0 : false) {
                                if (planning.isChevauche()) {
                                    Date date = addTimeToTimestamp(datesToTimestamp(planning.getDateFin(), planning.getHeureFin()), timeMargeAvance);
                                    if (!heure.after(date)) {
                                        deja = true;
                                    }
                                }
                            }
                            if (!deja) {
                                planning = new YvsGrhPlanningEmploye();
                                jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"calendrier", "jour"}, new Object[]{employe.getContrat().getCalendrier(), formatJour.format(heure)});
                                if (jour != null ? jour.getId() > 0 : false) {
                                    planning = defined(jour, heure);
                                }
                            }
                        }
                    }
                }
                if (planning != null) {
                    if (!planning.isSupplementaire()) {
                        Date date = timestampToDate(heure);
                        YvsJoursFeries jour = (YvsJoursFeries) dao.loadOneByNameQueries("YvsJoursFeries.findByJour", new String[]{"societe", "jour"}, new Object[]{employe.getAgence().getSociete(), date});
                        if (jour != null ? jour.getId() > 0 : false) {
                            planning.setSupplementaire(true);
                        }
                    }
                    planning.setHeureDebut(dateTimeWithOutSecond(datesToTimestamp(planning.getDateDebut(), planning.getHeureDebut())));
                    planning.setHeureFin(dateTimeWithOutSecond(datesToTimestamp(planning.getDateFin(), planning.getHeureFin())));
                }
                return planning;
            }
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPlanningEmploye.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public YvsGrhPlanningEmploye getPlanning(YvsGrhEmployes employe, Date heure) {
        try {
            YvsGrhPlanningEmploye planning = new YvsGrhPlanningEmploye();
            if (employe != null ? employe.getId() > 0 : false) {
                if (currentParamGrh != null ? currentParamGrh.getId() < 1 : true) {
                    loadParametreGrh(employe.getAgence().getSociete());
                }
                planning = getSimplePlanning(employe, heure);
                if (planning != null ? planning.getId() < 1 : true) {
                    planning = new YvsGrhPlanningEmploye();
                    if (currentParamGrh != null ? currentParamGrh.isPlanningDynamique() : false) {
                        YvsGrhTrancheHoraire tranche = null;// On cherche la tranche horaire correspondante
                        IYvsGrhTrancheHoraire impl = (IYvsGrhTrancheHoraire) IEntitiSax.createInstance("IYvsGrhTrancheHoraire", dao);
                        if (impl != null) {
                            tranche = impl.getTrancheHoraire(employe, heure);
                        }
                        if (tranche != null ? tranche.getId() > 0 : false) {
                            planning.setId(tranche.getId());
                            planning.setChevauche(tranche.isChevauche());
                            planning.setDateDebut(dateTimeWithOutSecond(datesToTimestamp(heure, tranche.getHeureDebut())));
                            planning.setDateFin(definedTimestamp(planning.getDateDebut(), tranche.getHeureFin()));
                            planning.setHeureDebut(tranche.getHeureDebut());
                            planning.setHeureFin(tranche.getHeureFin());
                            planning.setDureePause(tranche.getDureePause());
                            planning.setValide(false);
                        } else {
                            planning = defaut(heure);
                        }
                    } else {
                        planning = defaut(heure);
                    }
                    // On verifie si l'employé a un horaire dynamique
                    if (employe.getHoraireDynamique()) {// Si oui
                        // On verifi si le jour est un jour ouvrable
                        if (planning != null ? planning.getId() > 0 : false) {
                            IYvsJoursOuvres i_jour = (IYvsJoursOuvres) IEntitiSax.createInstance("IYvsJoursOuvres", dao);
                            if (i_jour != null) {
                                YvsJoursOuvres jour = i_jour.getJour(employe, planning.getDateDebut());
                                if (jour != null ? jour.getId() > 0 : false) {
                                    planning.setSupplementaire(currentParamGrh.getLimitHeureSup() > 0 ? (!jour.getOuvrable() || jour.getJourDeRepos()) : false);
                                }
                            }
                        }
                    }
                }
            }
            if (planning != null ? planning.getId() < 1 : true) {
                planning = defaut(heure);
                IYvsJoursOuvres i_jour = (IYvsJoursOuvres) IEntitiSax.createInstance("IYvsJoursOuvres", dao);
                if (i_jour != null) {
                    YvsJoursOuvres jour = i_jour.getJour(employe, planning.getDateDebut());
                    if (jour != null ? jour.getId() > 0 : false) {
                        planning.setSupplementaire(currentParamGrh.getLimitHeureSup() > 0 ? (!jour.getOuvrable() || jour.getJourDeRepos()) : false);
                    }
                }
            }
            if (planning != null) {
                if (!planning.isSupplementaire()) {
                    Date date = timestampToDate(heure);
                    YvsJoursFeries jour = (YvsJoursFeries) dao.loadOneByNameQueries("YvsJoursFeries.findByJour", new String[]{"societe", "jour"}, new Object[]{employe.getAgence().getSociete(), date});
                    if (jour != null ? jour.getId() > 0 : false) {
                        planning.setSupplementaire(true);
                    }
                }
                planning.setHeureDebut(dateTimeWithOutSecond(datesToTimestamp(planning.getDateDebut(), planning.getHeureDebut())));
                planning.setHeureFin(dateTimeWithOutSecond(datesToTimestamp(planning.getDateFin(), planning.getHeureFin())));
            }
            return planning;
        } catch (Exception ex) {
            Logger.getLogger(SYvsGrhPlanningEmploye.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
