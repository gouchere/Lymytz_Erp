/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.grh.presence;

import java.util.Date;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;

/**
 *
 * @author Lymytz Dowes
 */
public interface IYvsGrhPlanningEmploye {

    void setDao(DaoInterfaceLocal dao);

    YvsGrhPlanningEmploye defaut(Date heure);

    YvsGrhPlanningEmploye defined(YvsJoursOuvres jour, Date date);

    YvsGrhPlanningEmploye getSimplePlanning(YvsGrhEmployes employe, Date heure);

    YvsGrhPlanningEmploye getPlanning(YvsGrhEmployes employe, Date heure);

}
