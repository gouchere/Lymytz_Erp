/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseModeReglement;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseModeReglement {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseModeReglement> toString(String value);

    ResultatAction<YvsBaseModeReglement> controle(YvsBaseModeReglement entity);

    ResultatAction<YvsBaseModeReglement> save(YvsBaseModeReglement entity);

    ResultatAction<YvsBaseModeReglement> update(YvsBaseModeReglement entity);

    ResultatAction<YvsBaseModeReglement> delete(YvsBaseModeReglement entity);
}
