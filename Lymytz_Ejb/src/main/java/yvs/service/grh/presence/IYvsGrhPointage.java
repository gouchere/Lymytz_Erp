/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.grh.presence;

import java.util.Date;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhPointage;
import yvs.entity.grh.presence.YvsGrhPresence;
import yvs.entity.grh.presence.YvsPointeuse;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
public interface IYvsGrhPointage {

    void setDao(DaoInterfaceLocal dao);

    YvsGrhPointage defined(YvsGrhPresence presence, Date heure_entree, Date heure_sortie, YvsPointeuse pointeuse, boolean system_in, boolean system_out, YvsUsersAgence author);

    YvsGrhPointage defined(YvsGrhPresence presence, Date heure_entree, Date heure_sortie, boolean valider, YvsPointeuse pointeuse, boolean system_in, boolean system_out, YvsUsersAgence author);

    YvsGrhPointage defined(YvsGrhPresence presence, YvsGrhPointage pointage, Date heure_entree, Date heure_sortie, boolean valider, YvsPointeuse pointeuse, boolean system_in, boolean system_out, YvsUsersAgence author);
    
    boolean insertionPointage(YvsGrhEmployes employe, Date date, Date heure, YvsPointeuse pointeuse, int action, YvsUsersAgence author);
    
    boolean onSavePointage(YvsGrhEmployes employe, Date current_time, Date current_date, YvsPointeuse pointeuse, int action, YvsUsersAgence author);
    
    boolean onSavePointage(YvsGrhPresence presence, Date current_time, YvsPointeuse pointeuse, int action, YvsUsersAgence author);
    
    boolean onSavePointage(String mouvement, YvsGrhPointage pointage, YvsGrhPresence presence, Date current_time, YvsPointeuse pointeuse, YvsUsersAgence author);
    
}
