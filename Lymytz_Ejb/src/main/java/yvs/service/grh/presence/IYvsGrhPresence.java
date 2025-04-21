/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.grh.presence;

import java.util.Date;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;
import yvs.entity.grh.presence.YvsGrhPresence;

/**
 *
 * @author Lymytz Dowes
 */
public interface IYvsGrhPresence {

    void setDao(DaoInterfaceLocal dao);

    YvsGrhPresence defined(YvsGrhEmployes employe, YvsGrhPlanningEmploye planning);

    YvsGrhPresence getPresence(YvsGrhEmployes employe, YvsGrhPlanningEmploye planning);

    YvsGrhPresence getPresence(YvsGrhEmployes employe, Date current_time, boolean search_only);
    
}
