/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.grh.param;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import static yvs.service.AbstractEntity.formatJour;

/**
 *
 * @author Lymytz Dowes
 */
public class SYvsJoursOuvres extends AYvsJoursOuvres implements IYvsJoursOuvres {

    @Override
    public YvsJoursOuvres getJour(YvsGrhEmployes employe, Date date) {
        try {
            YvsCalendrier calendrier = null;
            if (employe != null) {
                if (employe.getContrat() != null ? (employe.getContrat().getCalendrier() != null ? employe.getContrat().getCalendrier().getId() > 0 : false) : false) {
                    calendrier = employe.getContrat().getCalendrier();
                } else {
                    calendrier = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByDefautSociete", new String[]{"societe"}, new Object[]{employe.getAgence().getSociete()});
                }
            }
            if (calendrier != null ? calendrier.getId() > 0 : false) {
                String dateToString = formatJour.format(date);
                return (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"calendrier", "jour"}, new Object[]{calendrier, dateToString});
            }
        } catch (Exception ex) {
            Logger.getLogger(SYvsJoursOuvres.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
