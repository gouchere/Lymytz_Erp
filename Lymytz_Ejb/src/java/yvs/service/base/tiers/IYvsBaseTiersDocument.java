/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.tiers.YvsBaseTiersDocument;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseTiersDocument  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseTiersDocument> toString(String value);

    ResultatAction<YvsBaseTiersDocument> controle(YvsBaseTiersDocument entity);

    ResultatAction<YvsBaseTiersDocument> save(YvsBaseTiersDocument entity);

    ResultatAction<YvsBaseTiersDocument> update(YvsBaseTiersDocument entity);

    ResultatAction<YvsBaseTiersDocument> delete(YvsBaseTiersDocument entity);
}
