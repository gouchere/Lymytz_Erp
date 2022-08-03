/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.emplacements;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.YvsComLiaisonDepot;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComLiaisonDepot {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComLiaisonDepot> toString(String value);

    ResultatAction<YvsComLiaisonDepot> controle(YvsComLiaisonDepot entity);

    ResultatAction<YvsComLiaisonDepot> save(YvsComLiaisonDepot entity);

    ResultatAction<YvsComLiaisonDepot> update(YvsComLiaisonDepot entity);

    ResultatAction<YvsComLiaisonDepot> delete(YvsComLiaisonDepot entity);
}
