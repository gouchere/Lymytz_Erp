/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.grh.activite.YvsGrhTypeCout;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsGrhTypeCout {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsGrhTypeCout> toString(String value);

    ResultatAction<YvsGrhTypeCout> controle(YvsGrhTypeCout entity);

    ResultatAction<YvsGrhTypeCout> save(YvsGrhTypeCout entity);

    ResultatAction<YvsGrhTypeCout> update(YvsGrhTypeCout entity);

    ResultatAction<YvsGrhTypeCout> delete(YvsGrhTypeCout entity);

}
