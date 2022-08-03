/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers.client;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.client.YvsComElementAddContratsClient;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComElementAddContratsClient  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComElementAddContratsClient> controle(YvsComElementAddContratsClient entity);

    ResultatAction<YvsComElementAddContratsClient> save(YvsComElementAddContratsClient entity);

    ResultatAction<YvsComElementAddContratsClient> update(YvsComElementAddContratsClient entity);

    ResultatAction<YvsComElementAddContratsClient> delete(YvsComElementAddContratsClient entity);
}
