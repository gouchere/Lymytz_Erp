/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Util;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhPresence;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsGrhTrancheHoraire extends AYvsGrhTrancheHoraire implements IYvsGrhTrancheHoraire {

    public SYvsGrhTrancheHoraire() {
    }

    public SYvsGrhTrancheHoraire(DaoInterfaceLocal dao) {
        super(dao);
    }

    @Override
    public YvsGrhTrancheHoraire getTrancheHoraire(YvsGrhEmployes employe, List<YvsGrhTrancheHoraire> tranches, List<YvsGrhTrancheHoraire> chevauches, Date heure) {
        try {
            Date timeMargeAvance = (currentParamGrh != null ? (currentParamGrh.getTimeMargeAvance() != null ? currentParamGrh.getTimeMargeAvance() : Util.getHeure(1, 0, 0)) : Util.getHeure(1, 0, 0));
            if (tranches != null ? !tranches.isEmpty() : false) {
                YvsGrhTrancheHoraire precedent = null, suivant = null;
                YvsGrhTrancheHoraire tranche = null;
                List<YvsGrhPresence> presences;
                Date heureDebut, heureFin;
                Date debut, fin;

                for (int i = 0; i < chevauches.size(); i++) {
                    tranche = chevauches.get(i);
                    heureDebut = addDay(dateTimeWithOutSecond(datesToTimestamp(heure, tranche.getHeureDebut())), -1);
                    heureFin = definedTimestamp(heureDebut, tranche.getHeureFin());
                    champ = new String[]{"dateDebut", "dateFin", "heureDebut", "heureFin", "employe"};
                    val = new Object[]{heureDebut, heureFin, heureDebut, heureFin, employe};
                    presences = dao.loadNameQueries("YvsGrhPresence.findByDatesTimesAndEmploye", champ, val);
                    for (YvsGrhPresence presence : presences) {
                        fin = addTimeToTimestamp(definedTimestamp(presence.getDateFin(), presence.getHeureFin()), timeMargeAvance);
                        if (!fin.before(heure)) {
                            tranche.setChevauche(true);
                            return tranche;
                        }
                    }
                }
                for (int i = 0; i < tranches.size(); i++) {
                    tranche = tranches.get(i);
                    heureDebut = dateTimeWithOutSecond(datesToTimestamp(heure, tranche.getHeureDebut()));
                    heureFin = definedTimestamp(heureDebut, tranche.getHeureFin());
                    if (heure.before(heureDebut)) {
                        if (i == 0) {
                            return tranche;
                        } else {
                            precedent = tranches.get(i - 1);
                            debut = addTimeToTimestamp(heure, timeMargeAvance);
                            fin = datesToTimestamp(heure, tranche.getHeureDebut());
                            if (debut.before(fin)) {
                                return precedent;
                            } else {
                                return tranche;
                            }
                        }
                    } else if (heureDebut.before(heure) && heure.before(heureFin)) {
                        if (i < tranches.size() - 1) {
                            suivant = tranches.get(i + 1);
                            debut = addTimeToTimestamp(heure, timeMargeAvance);
                            fin = datesToTimestamp(heure, suivant.getHeureDebut());
                            if (!debut.before(fin)) {
                                return suivant;
                            } else {
                                return tranche;
                            }
                        } else {
                            return tranche;
                        }
                    }
                }
                return tranche;
            }
        } catch (Exception ex) {
            Logger.getLogger(yvs.service.base.param.SYvsGrhTrancheHoraire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public YvsGrhTrancheHoraire getTrancheHoraire(YvsGrhEmployes employe, Date heure) {
        try {
            if (employe != null ? employe.getId() > 0 : false) {
                if (currentParamGrh != null ? currentParamGrh.getId() < 1 : true) {
                    loadParametreGrh(employe.getAgence().getSociete());
                }
                String type = employe.getContrat() != null ? employe.getContrat().getTypeTranche() : "JN";
                List<YvsGrhTrancheHoraire> tranches = dao.loadNameQueries("YvsGrhTrancheHoraire.findByTypeJournee", new String[]{"typeJournee", "societe"}, new Object[]{type, employe.getAgence().getSociete()});
                List<YvsGrhTrancheHoraire> chevauches = dao.loadNameQueries("YvsGrhTrancheHoraire.findChevaucheByTypeJournee", new String[]{"typeJournee", "societe"}, new Object[]{type, employe.getAgence().getSociete()});
                YvsGrhTrancheHoraire tranche = getTrancheHoraire(employe, tranches, chevauches, heure);
                if (tranche != null ? tranche.getId() < 1 : true) {
                    tranches = dao.loadNameQueries("YvsGrhTrancheHoraire.findAll", new String[]{"societe"}, new Object[]{employe.getAgence().getSociete()});
                    tranche = getTrancheHoraire(employe, tranches, null, heure);
                }
                return tranche;
            }
        } catch (Exception ex) {
            Logger.getLogger(yvs.service.base.param.SYvsGrhTrancheHoraire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
