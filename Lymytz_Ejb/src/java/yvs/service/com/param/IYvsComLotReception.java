/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.param;

import java.util.Date;
import java.util.List;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.achat.YvsComLotReception;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComLotReception {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComLotReception> controle(YvsComLotReception entity);

    ResultatAction<YvsComLotReception> save(YvsComLotReception entity);

    ResultatAction<YvsComLotReception> update(YvsComLotReception entity);

    ResultatAction<YvsComLotReception> delete(YvsComLotReception entity);
    
    List<YvsComLotReception> loadList(long depot, long conditionnement, Date date, double quantite, double stock);
}
