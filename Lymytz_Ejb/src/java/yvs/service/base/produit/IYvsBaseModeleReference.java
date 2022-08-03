/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseModeleReference;


/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseModeleReference  {
    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseModeleReference> toString(String value);

    ResultatAction<YvsBaseModeleReference> controle(YvsBaseModeleReference entity);

    ResultatAction<YvsBaseModeleReference> save(YvsBaseModeleReference entity);

    ResultatAction<YvsBaseModeleReference> update(YvsBaseModeleReference entity);

    ResultatAction<YvsBaseModeleReference> delete(YvsBaseModeleReference entity);
    
}
