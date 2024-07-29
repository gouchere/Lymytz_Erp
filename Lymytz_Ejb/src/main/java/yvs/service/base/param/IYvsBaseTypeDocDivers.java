/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseTypeDocDivers;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseTypeDocDivers {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseTypeDocDivers> toString(String value);

    ResultatAction<YvsBaseTypeDocDivers> controle(YvsBaseTypeDocDivers entity);

    ResultatAction<YvsBaseTypeDocDivers> save(YvsBaseTypeDocDivers entity);

    ResultatAction<YvsBaseTypeDocDivers> update(YvsBaseTypeDocDivers entity);

    ResultatAction<YvsBaseTypeDocDivers> delete(YvsBaseTypeDocDivers entity);
}
