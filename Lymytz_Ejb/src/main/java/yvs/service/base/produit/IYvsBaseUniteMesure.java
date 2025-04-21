/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseUniteMesure;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseUniteMesure  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseUniteMesure> toString(String value);

    ResultatAction<YvsBaseUniteMesure> controle(YvsBaseUniteMesure entity);

    ResultatAction<YvsBaseUniteMesure> save(YvsBaseUniteMesure entity);

    ResultatAction<YvsBaseUniteMesure> update(YvsBaseUniteMesure entity);

    ResultatAction<YvsBaseUniteMesure> delete(YvsBaseUniteMesure entity);
}
