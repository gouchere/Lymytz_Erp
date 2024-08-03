/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package yvs.timer.commercial;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.*;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import yvs.MyLog;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.service.IEntitySax;
import yvs.service.com.vente.IYvsComDocVentes;
import yvs.timer.InterfaceTimerLocal;

*/
/**
 *
 * @author LYMYTZ-PC
 *//*

@Singleton
public class TimerLivrerFacture implements InterfaceTimerLocal {

    @EJB
    public DaoInterfaceLocal dao;
    IEntitySax IEntitiSax = new IEntitySax();

//    @Schedule(dayOfMonth = "*", month = "*", year = "*", hour = "23", dayOfWeek = "*", minute = "59", persistent = false)
    @Override
    public void myTimer() {
//        execute();
    }

    @Timeout
    public void timeout() {

    }

    @Override
    public void avancement() {
        //-
    }

    private void execute() {
        MyLog.Write(getClass(), "execute", true, true);
        try {
            IYvsComDocVentes i_facture = (IYvsComDocVentes) IEntitiSax.createInstance("IYvsComDocVentes", dao);
            if (i_facture != null) {
                List<YvsComDocVentes> factures = dao.loadNameQueries("YvsComDocVentes.factureByNotLivreAndWait", new String[]{}, new Object[]{});
                for (YvsComDocVentes d : factures) {
                    try {
                        if (d.getEnteteDoc() == null) {
                            continue;
                        }
                        if (d.getEnteteDoc().getAgence() == null) {
                            continue;
                        }
                        if (d.getEnteteDoc().getCreneau() == null) {
                            continue;
                        }
                        if (d.getEnteteDoc().getCreneau().getCreneauPoint() == null) {
                            continue;
                        }
                        i_facture.livraison(d.getDocuments(), d, d.getAuthor(), d.getEnteteDoc().getAgence().getSociete(), d.getEnteteDoc().getAgence(), d.getDepotLivrer(), d.getEnteteDoc().getCreneau().getCreneauPoint().getPoint());
                    } catch (Exception ex) {
                        Logger.getLogger(TimerLivrerFacture.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TimerLivrerFacture.class.getName()).log(Level.SEVERE, null, ex);
        }
        MyLog.Write(getClass(), "execute", true, false);
    }
}
*/
