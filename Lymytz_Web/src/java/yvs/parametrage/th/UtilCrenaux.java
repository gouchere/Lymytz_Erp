/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.th;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.param.YvsCrenauxHoraire;

/**
 *
 * @author GOUCHERE YVES
 */
public class UtilCrenaux {

    @EJB
    public static DaoInterfaceLocal dao;

    public UtilCrenaux() {
    }

    public static YvsCrenauxHoraire buildBeanDepot(CrenauxHoraire cr) {
        YvsCrenauxHoraire c = new YvsCrenauxHoraire(cr.getId());
        c.setActif(cr.isActif());
        c.setCodeTranche(cr.getCodeTranche());
        c.setHeureDeb(cr.getHeureDeb());
        c.setHeureFin(cr.getHeureFin());
        c.setLibelle(cr.getLibelle());
        c.setOrdre(cr.getOrdre());
        c.setTypeDeJournee(cr.getTypeDeJournee());
        return c;
    }

    public static CrenauxHoraire buildBeanDepot(YvsCrenauxHoraire cr) {
        CrenauxHoraire c = new CrenauxHoraire(cr.getId());
        c.setActif(cr.getActif());
        c.setCodeTranche(cr.getCodeTranche());
        c.setHeureDeb(cr.getHeureDeb());
        c.setHeureFin(cr.getHeureFin());
        c.setLibelle(cr.getLibelle());
        c.setOrdre(cr.getOrdre());
        c.setTypeDeJournee(cr.getTypeDeJournee());
        return c;
    }

    public static List<CrenauxHoraire> buildBeanDepot(List<YvsCrenauxHoraire> l) {
        List<CrenauxHoraire> result = new ArrayList<>();
        for (YvsCrenauxHoraire d : l) {
            result.add(buildBeanDepot(d));
        }
        return result;
    }
}
