/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBasePlanTarifaire {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBasePlanTarifaire> toString(String value);

    ResultatAction<YvsBasePlanTarifaire> controle(YvsBasePlanTarifaire entity);

    ResultatAction<YvsBasePlanTarifaire> save(YvsBasePlanTarifaire entity);

    ResultatAction<YvsBasePlanTarifaire> update(YvsBasePlanTarifaire entity);

    ResultatAction<YvsBasePlanTarifaire> delete(YvsBasePlanTarifaire entity);
}
