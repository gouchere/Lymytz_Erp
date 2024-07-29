/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.grh.param;

import java.util.Date;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;

/**
 *
 * @author Lymytz Dowes
 */
public interface IYvsJoursOuvres {

    void setDao(DaoInterfaceLocal dao);

    YvsJoursOuvres getJour(YvsGrhEmployes employe, Date date);
}
