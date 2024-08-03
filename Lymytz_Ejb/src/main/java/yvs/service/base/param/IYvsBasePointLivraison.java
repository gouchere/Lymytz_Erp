/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBasePointLivraison;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBasePointLivraison {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBasePointLivraison> toString(String value);

    ResultatAction<YvsBasePointLivraison> controle(YvsBasePointLivraison entity);

    ResultatAction<YvsBasePointLivraison> save(YvsBasePointLivraison entity);

    ResultatAction<YvsBasePointLivraison> update(YvsBasePointLivraison entity);

    ResultatAction<YvsBasePointLivraison> delete(YvsBasePointLivraison entity);
}
