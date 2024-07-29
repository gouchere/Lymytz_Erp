/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.client.YvsComCategorieTarifaire;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComCategorieTarifaire  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComCategorieTarifaire> toString(String value);

    ResultatAction<YvsComCategorieTarifaire> controle(YvsComCategorieTarifaire entity);

    ResultatAction<YvsComCategorieTarifaire> save(YvsComCategorieTarifaire entity);

    ResultatAction<YvsComCategorieTarifaire> update(YvsComCategorieTarifaire entity);

    ResultatAction<YvsComCategorieTarifaire> delete(YvsComCategorieTarifaire entity);
}
