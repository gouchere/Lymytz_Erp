/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.grh;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsPointeuse;
import yvs.grh.presence.IOEMDevice;
import yvs.grh.presence.ParametreIOEM;
import yvs.service.grh.presence.IYvsGrhPointage;

/**
 *
 * @author Lymytz Dowes
 */
@Path("services/grh")
@RequestScoped
public class GenericResource extends lymytz.ws.GenericResource {

    @POST
    @Path("value")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String value(String value) {
        System.out.println("value : " + value);
        return "Succès";
    }

    @POST
    @Path("synchronise_pointage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ResultatAction> synchronisePointage(ParametreIOEM param) {
        List<ResultatAction> result = new ArrayList<>();
        try {
            IYvsGrhPointage i_pointage = (IYvsGrhPointage) IEntitiSax.createInstance("IYvsGrhPointage", dao);
            if (i_pointage != null && (param != null ? param.getSociete() > 0 : false)) {
                List<IOEMDevice> pointages = param.getPointages();
                if (pointages != null ? !pointages.isEmpty() : false) {
                    String query = "DELETE FROM yvs_grh_presence WHERE employe In (SELECT e.id FROM yvs_grh_employes e INNER JOIN yvs_agences a ON e.agence = a.id WHERE a.societe = ?";
                    int position = 1;
                    List<Options> params = new ArrayList<>();
                    params.add(new Options(param.getSociete(), position++));
                    if (param.isAddEmploye() ? (param.getEmploye() > 0) : false) {
                        query += " AND e.id = ?";
                        params.add(new Options(param.getEmploye(), position++));
                    }
                    query += ")";
                    if (param.isAddEmploye()) {
                        query += " AND date_debut BETWEEN ? AND ?";
                        params.add(new Options(param.getDateDebut(), position++));
                        params.add(new Options(param.getDateFin(), position++));
                    }
                    if (param.isInvalid()) {
                        query += " AND valider = false";
                    }
                    dao.requeteLibre(query, params.toArray(new Options[params.size()]));
                    YvsGrhEmployes employe;
                    Calendar time = Calendar.getInstance();
                    for (IOEMDevice p : pointages) {
                        if (!p.inclure) {
                            employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{(long) p.idwSEnrollNumber});
                            if (employe != null ? employe.getId() > 0 : false) {
                                time.set(Calendar.DATE, p.idwDay);
                                time.set(Calendar.MONTH, p.idwMonth - 1);
                                time.set(Calendar.YEAR, p.idwYear);
                                time.set(Calendar.HOUR, p.idwHour);
                                time.set(Calendar.MINUTE, p.idwMinute);
                                time.set(Calendar.SECOND, p.idwSecond);

                                if (i_pointage.insertionPointage(employe, time.getTime(), time.getTime(), new YvsPointeuse(p.pointeuse != null ? p.pointeuse.getId() : 0), p.idwInOutMode, null)) {
                                    result.add(new ResultatAction(true, p, 0L, "Succès"));
                                } else {
                                    result.add(new ResultatAction(false, p, 0L, "Echec"));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(lymytz.ws.grh.GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
