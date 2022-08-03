/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.client.YvsComClient;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComClient  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComClient> toString(String value);

    ResultatAction<YvsComClient> controle(YvsComClient entity);

    ResultatAction<YvsComClient> save(YvsComClient entity);

    ResultatAction<YvsComClient> update(YvsComClient entity);

    ResultatAction<YvsComClient> delete(YvsComClient entity);
}
