/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseParametre;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseParametre {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseParametre> toString(String value);

    ResultatAction<YvsBaseParametre> controle(YvsBaseParametre entity);

    ResultatAction<YvsBaseParametre> save(YvsBaseParametre entity);

    ResultatAction<YvsBaseParametre> update(YvsBaseParametre entity);

    ResultatAction<YvsBaseParametre> delete(YvsBaseParametre entity);

}
