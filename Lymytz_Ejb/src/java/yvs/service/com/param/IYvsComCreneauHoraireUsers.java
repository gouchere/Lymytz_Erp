/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComCreneauHoraireUsers {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComCreneauHoraireUsers> controle(YvsComCreneauHoraireUsers entity);

    ResultatAction<YvsComCreneauHoraireUsers> save(YvsComCreneauHoraireUsers entity);

    ResultatAction<YvsComCreneauHoraireUsers> update(YvsComCreneauHoraireUsers entity);

    ResultatAction<YvsComCreneauHoraireUsers> delete(YvsComCreneauHoraireUsers entity);
}
