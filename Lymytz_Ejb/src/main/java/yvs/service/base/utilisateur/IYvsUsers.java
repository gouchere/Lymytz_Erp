/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.utilisateur;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.users.YvsUsers;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsUsers  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsUsers> toString(String value);

    ResultatAction<YvsUsers> controle(YvsUsers entity);

    ResultatAction<YvsUsers> save(YvsUsers entity);

    ResultatAction<YvsUsers> update(YvsUsers entity);

    ResultatAction<YvsUsers> delete(YvsUsers entity);
}
