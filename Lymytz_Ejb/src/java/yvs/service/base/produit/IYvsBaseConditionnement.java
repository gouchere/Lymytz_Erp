/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseConditionnement;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseConditionnement {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseConditionnement> toString(String value);

    ResultatAction<YvsBaseConditionnement> controle(YvsBaseConditionnement entity);

    ResultatAction<YvsBaseConditionnement> save(YvsBaseConditionnement entity);

    ResultatAction<YvsBaseConditionnement> update(YvsBaseConditionnement entity);

    ResultatAction<YvsBaseConditionnement> delete(YvsBaseConditionnement entity);
}
