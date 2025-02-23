/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaTaxeDocDivers {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaTaxeDocDivers> toString(String value);

    ResultatAction<YvsComptaTaxeDocDivers> controle(YvsComptaTaxeDocDivers entity);

    ResultatAction<YvsComptaTaxeDocDivers> save(YvsComptaTaxeDocDivers entity);

    ResultatAction<YvsComptaTaxeDocDivers> update(YvsComptaTaxeDocDivers entity);

    ResultatAction<YvsComptaTaxeDocDivers> delete(YvsComptaTaxeDocDivers entity);
}
