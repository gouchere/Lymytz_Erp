/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhPointage;
import yvs.entity.grh.presence.YvsGrhPresence;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsSocietes;
import yvs.grh.bean.Employe;
import yvs.grh.bean.PlanningTravail;
import static yvs.util.Util.dj;

/**
 *
 * @author Lymytz Dowes
 */
public class ActionsPointage {

    static String result = "";
    @EJB
    public DaoInterfaceLocal dao;

    private YvsGrhPresence getPresence(PlanningTravail p, Employe e) {
        YvsGrhPresence pe = new YvsGrhPresence();
        pe.setDateDebut(p.getDateDebut());
        pe.setDateFin(p.getDateFin());
        pe.setEmploye(new YvsGrhEmployes(e.getId()));
        pe.setHeureDebut(p.getHeureDebut());
        pe.setHeureFin(p.getHeureFin());
        pe.setValider(p.isValider());
        pe.setSupplementaire(p.isSupplementaire());
        pe.setDureePause(p.getPause());
        return pe;
    }

    private YvsGrhPointage getPointage(YvsGrhPresence pe, Date heureEntree, Date heureSortie, boolean valider, Object pointeuse, boolean systemInt, boolean systemOut) {
        return getPointage(pe, 0, heureEntree, heureSortie, valider, pointeuse, systemInt, systemOut);
    }

    private YvsGrhPointage getPointage(YvsGrhPresence pe, long id, Date heureSortie, boolean valider, Object pointeuse, boolean systemInt, boolean systemOut) {
        return getPointage(pe, id, null, heureSortie, valider, pointeuse, systemInt, systemOut);
    }

    private YvsGrhPointage getPointage(YvsGrhPresence pe, Date heureEntree, boolean valider, Object pointeuse, boolean systemInt, boolean systemOut) {
        return getPointage(pe, 0, heureEntree, null, valider, pointeuse, systemInt, systemOut);
    }

    private YvsGrhPointage getPointage(YvsGrhPresence pe, long id, Date heureEntree, Date heureSortie, boolean valider, Object pointeuse, boolean systemInt, boolean systemOut) {
        YvsGrhPointage p = new YvsGrhPointage(id);
        p.setHeureEntree(heureEntree);
        p.setHeureSortie(heureSortie);
        p.setPresence(pe);
        p.setHeureSupplementaire(pe.isSupplementaire());
        p.setValider((!pe.isSupplementaire() ? valider : true));
        return p;
    }

    private YvsGrhTrancheHoraire returnTranche(Date heure, List<YvsGrhTrancheHoraire> lt, YvsParametreGrh parametre) {
        YvsGrhTrancheHoraire p = new YvsGrhTrancheHoraire();
        if (lt != null ? !lt.isEmpty() : false) {
            for (int i = 0; i < lt.size(); i++) {
                YvsGrhTrancheHoraire t = lt.get(i);

                Calendar d = Calendar.getInstance();
                d.setTime(t.getHeureDebut());
                Calendar f = Calendar.getInstance();
                f.setTime(t.getHeureFin());

                Calendar c = Calendar.getInstance();
                c.setTime(heure);
                c.set(Calendar.HOUR_OF_DAY, d.get(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, d.get(Calendar.MINUTE));
                c.set(Calendar.SECOND, 0);

                Date heureDebut = c.getTime();
                d.setTime(heureDebut);

                c.set(Calendar.HOUR_OF_DAY, f.get(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, f.get(Calendar.MINUTE));
                c.set(Calendar.SECOND, 0);
                if (d.after(c)) {
                    c.add(Calendar.DAY_OF_YEAR, 1);
                }

                f = c;
                c.setTime(heure);
                if (c.before(d)) {
                    if (i == 0) {
                        p.setId(t.getId());
                        p.setHeureDebut(t.getHeureDebut());
                        p.setHeureFin(t.getHeureFin());
                        p.setDureePause(t.getDureePause());
                    } else {
                        YvsGrhTrancheHoraire t_ = lt.get(i - 1);

                        Calendar m = Calendar.getInstance();
                        m.setTime(parametre.getTimeMargeAvance());

                        c.setTime(heure);
                        c.add(Calendar.HOUR_OF_DAY, m.get(Calendar.HOUR_OF_DAY));
                        c.add(Calendar.MINUTE, m.get(Calendar.MINUTE));
                        c.add(Calendar.SECOND, 0);

                        if (c.before(d)) {
                            p.setId(t_.getId());
                            p.setHeureDebut(t_.getHeureDebut());
                            p.setHeureFin(t_.getHeureFin());
                            p.setDureePause(t_.getDureePause());
                        } else {
                            p.setId(t.getId());
                            p.setHeureDebut(t.getHeureDebut());
                            p.setHeureFin(t.getHeureFin());
                            p.setDureePause(t.getDureePause());
                        }
                    }
                    break;
                } else if (d.before(c) && c.before(f)) {
                    if (i < lt.size() - 1) {
                        YvsGrhTrancheHoraire t_ = lt.get(i + 1);

                        Calendar m = Calendar.getInstance();
                        m.setTime(parametre.getTimeMargeAvance());

                        c.setTime(heure);
                        c.add(Calendar.HOUR_OF_DAY, m.get(Calendar.HOUR_OF_DAY));
                        c.add(Calendar.MINUTE, m.get(Calendar.MINUTE));
                        c.add(Calendar.SECOND, 0);

                        if (!c.before(d)) {
                            p.setId(t_.getId());
                            p.setHeureDebut(t_.getHeureDebut());
                            p.setHeureFin(t_.getHeureFin());
                            p.setDureePause(t_.getDureePause());
                        } else {
                            p.setId(t.getId());
                            p.setHeureDebut(t.getHeureDebut());
                            p.setHeureFin(t.getHeureFin());
                            p.setDureePause(t.getDureePause());
                        }
                    } else {
                        p.setId(t.getId());
                        p.setHeureDebut(t.getHeureDebut());
                        p.setHeureFin(t.getHeureFin());
                        p.setDureePause(t.getDureePause());
                    }
                }

            }
        }
        return p;
    }

    private YvsGrhPresence returnPresence(YvsGrhEmployes e, PlanningTravail p, YvsSocietes societe) {
        try {
            if ((e != null ? e.getId() > 0 : false) && (p != null ? p.getId() > 0 : false)) {
                YvsGrhPresence p_ = (YvsGrhPresence) dao.loadOneByNameQueries("YvsGrhPresence.findOneFiches", new String[]{"employe", "dateDebut", "dateFin"}, new Object[]{e, p.getDateDebut(), p.getDateFin()});
                if (p_ != null ? p_.getId() > 0 : false) {
                    Calendar c = Calendar.getInstance();

                    c.setTime(p_.getHeureDebut());
                    c.set(Calendar.SECOND, 0);
                    p_.setHeureDebut(c.getTime());

                    c.setTime(p_.getHeureFin());
                    c.set(Calendar.SECOND, 0);
                    p_.setHeureFin(c.getTime());

                    boolean b = false;
                    if (e.getContrat() != null ? e.getContrat().getId() > 0 : false) {
                        if (e.getContrat().getCalendrier() != null ? e.getContrat().getCalendrier().getId() > 0 : false) {
                            b = true;
                            YvsJoursOuvres jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"calendrier", "jour"}, new Object[]{e.getContrat().getCalendrier(), dj.format(p_.getDateDebut())});
                            if (jour != null ? jour.getId() > 0 : false) {
                                p_.setSupplementaire(!jour.getOuvrable());
                            }
                        }
                    }
                    if (!b) {
                        YvsCalendrier cal = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByDefautSociete", new String[]{"societe"}, new Object[]{societe});
                        YvsJoursOuvres jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"calendrier", "jour"}, new Object[]{cal, dj.format(p_.getDateDebut())});
                        if (jour != null ? jour.getId() > 0 : false) {
                            p_.setSupplementaire(!jour.getOuvrable());
                        }
                    }
                    System.out.println("presence :" + p_.getId() + " - pe.getHeureDebut() :" + p_.getHeureDebut() + " - pe.getHeureFin() :" + p_.getHeureFin());
                    return p_;
                }
            }
            return null;
        } catch (Exception ex) {
            System.err.println("Error : " + ex.getMessage());
            return null;
        }
    }

    public static boolean insertPointage(Date heure_, YvsGrhEmployes currentEmploye, YvsSocietes societe, YvsParametreGrh parametre) {
        try {
//            return insertPointage(currentEmploye, df.parse(df.format(heure_)), heure_, societe, parametre);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ActionsPointage.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean insertPointage(YvsGrhEmployes employe_, Date date_, Date heure_, YvsSocietes societe, YvsParametreGrh parametre) {
        Calendar c = Calendar.getInstance();
        c.setTime(heure_);
        c.set(Calendar.SECOND, 0);
        Date h = c.getTime();

//        List<Pointage> lp = PointageBll.horaires(employe_, h);
//        if (lp != null ? lp.isEmpty() : true) {
//            return onSavePointage(employe_, heure_, date_, societe, parametre);
//        }
        return false;
    }

    public static String returnMouvement(Date heure_, YvsGrhEmployes currentEmploye, YvsSocietes societe, YvsParametreGrh parametre) {
        try {
//            return returnMouvement(currentEmploye, heure_, df.parse(df.format(heure_)), societe, parametre);
            return "S";
        } catch (Exception ex) {
            Logger.getLogger(ActionsPointage.class.getName()).log(Level.SEVERE, null, ex);
            return "ES ParseException : " + ex.getMessage();
        }
    }
}
