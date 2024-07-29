/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.commission.YvsComCommissionCommerciaux;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComCommissionCommerciaux {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComCommissionCommerciaux> controle(YvsComCommissionCommerciaux entity);

    ResultatAction<YvsComCommissionCommerciaux> save(YvsComCommissionCommerciaux entity);

    ResultatAction<YvsComCommissionCommerciaux> update(YvsComCommissionCommerciaux entity);

    ResultatAction<YvsComCommissionCommerciaux> delete(YvsComCommissionCommerciaux entity);
}
