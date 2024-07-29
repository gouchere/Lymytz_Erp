/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseClassesStat;

/**
 *
 * @author Lymytz Dowes
 */
public interface IYvsBaseClassesStat {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseClassesStat> toString(String value);

    ResultatAction<YvsBaseClassesStat> controle(YvsBaseClassesStat entity);

    ResultatAction<YvsBaseClassesStat> save(YvsBaseClassesStat entity);

    ResultatAction<YvsBaseClassesStat> update(YvsBaseClassesStat entity);

    ResultatAction<YvsBaseClassesStat> delete(YvsBaseClassesStat entity);
}
