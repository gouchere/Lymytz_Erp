/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.timer.mutuelle;

import java.util.Calendar;
import java.util.Date;
import java.util.List;                              
import yvs.dao.*;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.mutuelle.echellonage.YvsMutEchellonage;
import yvs.entity.mutuelle.echellonage.YvsMutMensualite;
import yvs.timer.InterfaceTimerLocal;

/**
 *
 * @author LYMYTZ-PC
 */
public class TimerAddMensualite {
/*
    @EJB
    public DaoInterfaceLocal dao;

    @Schedule(dayOfMonth = "*", month = "*", year = "*", hour = "23", dayOfWeek = "6", minute = "59", persistent = false)
    @Override
    public void myTimer() {
//        onVerifyMensualite();
    }

    @Timeout
    public void timeout() {

    }

    @Override
    public void avancement() {
        //-
    }

    public boolean onVerifyMensualite() {
        try {
            List<YvsMutEchellonage> list = dao.loadNameQueries("YvsMutEchellonage.findNotByEtats", new String[]{"etat"}, new Object[]{"V, T, P"});
            for (YvsMutEchellonage y : list) {
                onVerifyMensualite(y); 
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean onVerifyMensualite(YvsMutEchellonage y) {
        try {
            List<YvsMutMensualite> list = dao.loadNameQueries("YvsMutMensualite.findLastByEchellonage", new String[]{"echellonage"}, new Object[]{y}, 0, 1);
            YvsMutMensualite m = null;
            if (list != null ? !list.isEmpty() : false) {
                m = list.get(0);
            }
            if (m != null ? m.getId() > 0 : false) {
                Calendar c = Calendar.getInstance();
                c.setTime(m.getDateMensualite());
                int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                c.add(Calendar.DATE, day);

                if (!c.getTime().before(new Date())) {
                    m = new YvsMutMensualite(null, m);
                    m.setDateMensualite(c.getTime());
                    m.setMontant(m.getInteret());
                    m.setAmortissement(0D);
                    m.setMontantPenalite(0D);
                    m.setDateSave(new Date());
                    m.setDateUpdate(new Date());
                    m.setEtat(Constantes.ETAT_ATTENTE);
                    m.setCommentaire("Pénélité générée a cause d'un retard");
                    dao.save(m);
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }*/
}
