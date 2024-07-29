/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.timer.commercial;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.*;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.param.YvsSocietes;
import yvs.timer.InterfaceTimerLocal;

/**
 *
 * @author LYMYTZ-PC
 */
@Singleton
public class TimerDeactiveClient implements InterfaceTimerLocal {

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
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, TimerDeactiveClient.class.getName(), ex);
        }
        return false;
    }

    public boolean onDesactive(long societe) {
        try {
            List<YvsComParametre> l = dao.loadNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(societe)}, 0, 1);
            if (l != null ? !l.isEmpty() : false) {
                if (l.get(0).getDureeInactiv() > 0) {
                    String query = "update yvs_com_client set actif = false where id IN (select c.id from yvs_com_client c inner join yvs_base_tiers t on c.tiers = t.id where c.actif is true and t.societe = ? and current_date - ? > (select e.date_entete from yvs_com_doc_ventes d inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc where d.client = c.id order by e.date_entete desc limit 1))";
                    Options[] params = new Options[]{new Options(societe, 1), new Options(l.get(0).getDureeInactiv(), 2)};
                    dao.requeteLibre(query, params);
                }
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, TimerDeactiveClient.class.getName(), ex);
        }
        return false;
    }

    public List<Long> loadClient() {
        List<Long> ids = new ArrayList<>();
        List<YvsSocietes> l = dao.loadNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
        List<Long> id;
        for (YvsSocietes s : l) {
            id = loadClient(s.getId());
            if (id != null) {
                ids.addAll(id);
            }
        }
        return ids;
    }

    public List<Long> loadClient(long societe) {
        List<YvsComParametre> l = dao.loadNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(societe)}, 0, 1);
        if (l != null ? !l.isEmpty() : false) {
            int ecart = l.get(0).getDureeInactiv();
            String query = "select c.id from yvs_com_client c where actif is true and current_date - ? > (select e.date_entete from yvs_com_doc_ventes d inner join yvs_com_entete_doc_vente e on e.id = d.entete_doc where d.client = c.id order by e.date_entete desc limit 1)";
            Options[] params = new Options[]{new Options(ecart, 1)};
            List<Long> ids = dao.loadListBySqlQuery(query, params);
            return ids;
        }
        return null;
    }
}
