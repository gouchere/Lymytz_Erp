/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers.client;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.client.YvsComElementContratsClient;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComElementContratsClient  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComElementContratsClient> controle(YvsComElementContratsClient entity);

    ResultatAction<YvsComElementContratsClient> save(YvsComElementContratsClient entity);

    ResultatAction<YvsComElementContratsClient> update(YvsComElementContratsClient entity);

    ResultatAction<YvsComElementContratsClient> delete(YvsComElementContratsClient entity);
}
