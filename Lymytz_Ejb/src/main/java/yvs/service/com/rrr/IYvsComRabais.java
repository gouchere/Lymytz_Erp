/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.rrr;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.rrr.YvsComRabais;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComRabais {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComRabais> toString(String value);

    ResultatAction<YvsComRabais> controle(YvsComRabais entity);

    ResultatAction<YvsComRabais> save(YvsComRabais entity);

    ResultatAction<YvsComRabais> update(YvsComRabais entity);

    ResultatAction<YvsComRabais> delete(YvsComRabais entity);
}
