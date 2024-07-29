/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.timer.mutuelle;

import java.util.List;
import yvs.dao.*;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import yvs.entity.mutuelle.YvsMutParametre;
import yvs.entity.param.YvsSocietes;
import yvs.timer.InterfaceTimerLocal;

/**
 *
 * @author LYMYTZ-PC
 */
@Singleton
public class TimerAnnuleCredit implements InterfaceTimerLocal {

    @EJB
    public DaoInterfaceLocal dao;

    @Schedule(dayOfMonth = "*", month = "*", year = "*", hour = "23", dayOfWeek = "6", minute = "59", persistent = false)
    @Override
    public void myTimer() {
//        onDesactive();
    }

    @Timeout
    public void timeout() {

    }

    @Override
    public void avancement() {
        //-
    }

    public boolean onDesactive() {
        try {
            List<YvsSocietes> l = dao.loadNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
            for (YvsSocietes s : l) {
                onDesactive(s.getId());
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean onDesactive(long societe) {
        try {
            List<YvsMutParametre> l = dao.loadNameQueries("YvsMutParametre.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(societe)}, 0, 1);
//            if (l != null ? !l.isEmpty() : false) {
//                if (l.get(0).getDureeEtudeCredit() > 0) {
//                    String query = "update yvs_mut_credit set etat = 'A' where id IN (SELECT y.id FROM yvs_mut_credit y INNER JOIN yvs_mut_type_credit t ON y.type = t.id "
//                            + "INNER JOIN yvs_mut_mutuelle m ON t.mutuelle = m.id "
//                            + "WHERE m.societe = ? AND current_date - ? > y.date_soumission AND y.etat NOT IN ('V','P'))";
//                    Options[] params = new Options[]{new Options(societe, 1), new Options(l.get(0).getDureeEtudeCredit(), 2)};
//                    dao.requeteLibre(query, params);
//                }
//            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
