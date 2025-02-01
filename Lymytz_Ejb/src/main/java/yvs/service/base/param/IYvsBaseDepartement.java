/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepartement;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseDepartement {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseDepartement> toString(String value);

    ResultatAction<YvsBaseDepartement> controle(YvsBaseDepartement entity);

    ResultatAction<YvsBaseDepartement> save(YvsBaseDepartement entity);

    ResultatAction<YvsBaseDepartement> update(YvsBaseDepartement entity);

    ResultatAction<YvsBaseDepartement> delete(YvsBaseDepartement entity);
}
