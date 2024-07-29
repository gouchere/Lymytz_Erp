/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.emplacements;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepots;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseDepots  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseDepots> toString(String value);

    ResultatAction<YvsBaseDepots> controle(YvsBaseDepots entity);

    ResultatAction<YvsBaseDepots> save(YvsBaseDepots entity);

    ResultatAction<YvsBaseDepots> update(YvsBaseDepots entity);

    ResultatAction<YvsBaseDepots> delete(YvsBaseDepots entity);

}
