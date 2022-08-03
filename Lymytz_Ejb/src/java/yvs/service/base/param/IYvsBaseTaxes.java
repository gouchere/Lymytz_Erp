/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseTaxes;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseTaxes {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseTaxes> toString(String value);

    ResultatAction<YvsBaseTaxes> controle(YvsBaseTaxes entity);

    ResultatAction<YvsBaseTaxes> save(YvsBaseTaxes entity);

    ResultatAction<YvsBaseTaxes> update(YvsBaseTaxes entity);

    ResultatAction<YvsBaseTaxes> delete(YvsBaseTaxes entity);
}
