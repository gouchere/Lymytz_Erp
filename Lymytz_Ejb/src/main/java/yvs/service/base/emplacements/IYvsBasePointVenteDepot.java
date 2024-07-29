/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.emplacements;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBasePointVenteDepot;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBasePointVenteDepot {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBasePointVenteDepot> toString(String value);

    ResultatAction<YvsBasePointVenteDepot> controle(YvsBasePointVenteDepot entity);

    ResultatAction<YvsBasePointVenteDepot> save(YvsBasePointVenteDepot entity);

    ResultatAction<YvsBasePointVenteDepot> update(YvsBasePointVenteDepot entity);

    ResultatAction<YvsBasePointVenteDepot> delete(YvsBasePointVenteDepot entity);
}
