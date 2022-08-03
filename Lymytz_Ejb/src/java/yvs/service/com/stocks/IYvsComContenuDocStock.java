/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.stocks;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.stock.YvsComContenuDocStock;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComContenuDocStock {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComContenuDocStock> toString(String value);

    ResultatAction<YvsComContenuDocStock> controle(YvsComContenuDocStock entity);

    ResultatAction<YvsComContenuDocStock> save(YvsComContenuDocStock entity);

    ResultatAction<YvsComContenuDocStock> update(YvsComContenuDocStock entity);

    ResultatAction<YvsComContenuDocStock> delete(YvsComContenuDocStock entity);
}
