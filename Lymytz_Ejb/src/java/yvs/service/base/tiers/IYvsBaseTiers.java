/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.tiers.YvsBaseTiers;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseTiers  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseTiers> toString(String value);

    ResultatAction<YvsBaseTiers> controle(YvsBaseTiers entity);

    ResultatAction<YvsBaseTiers> save(YvsBaseTiers entity);

    ResultatAction<YvsBaseTiers> update(YvsBaseTiers entity);

    ResultatAction<YvsBaseTiers> delete(YvsBaseTiers entity);
}
