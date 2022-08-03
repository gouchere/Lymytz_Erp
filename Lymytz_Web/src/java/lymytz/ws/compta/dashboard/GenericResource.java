/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws.compta.dashboard;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import yvs.entity.param.YvsSocietes;
import yvs.service.dashboards.commercial.AbstractDashBoadCom;

/**
 *
 * @author Lymytz Dowes
 */
@Path("services/compta/dashboard")
@RequestScoped
public class GenericResource extends lymytz.ws.GenericResource {

    @POST
    @Path("getTotalCaisses")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Object[]> getTotalCaisses(@HeaderParam("societe") String societe, @HeaderParam("agence") String agence, @HeaderParam("caisse") String caisse, @HeaderParam("mode") String mode, @HeaderParam("table") String table, @HeaderParam("mouvement") String mouvement, @HeaderParam("type") String type, @HeaderParam("statut") String statut, @HeaderParam("date_debut") String date_debut, @HeaderParam("date_fin") String date_fin, @HeaderParam("periode") String periode) {
        try {
            Date debut;
            if (!asString(date_debut)) {
                debut = (Date) dao.loadObjectByNameQueries("YvsComptaMouvementCaisse.findFirstDate", new String[]{"societe"}, new Object[]{new YvsSocietes(Long.valueOf(societe))});
                periode = "";
            } else {
                debut = df.parse(date_debut);
            }
            Date fin = df.parse(date_fin);
            String rq = "select y.* from public.compta_total_caisses(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) y order by rang, caisse desc";
            return dao.loadDataByNativeQuery(rq, new Object[]{Long.valueOf(societe), Long.valueOf(agence), caisse, Long.valueOf(mode), table, mouvement, type, statut, debut, fin, periode});
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(AbstractDashBoadCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
//
