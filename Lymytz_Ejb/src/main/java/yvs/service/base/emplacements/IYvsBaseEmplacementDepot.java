/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.emplacements;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseEmplacementDepot;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseEmplacementDepot {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseEmplacementDepot> toString(String value);

    ResultatAction<YvsBaseEmplacementDepot> controle(YvsBaseEmplacementDepot entity);

    ResultatAction<YvsBaseEmplacementDepot> save(YvsBaseEmplacementDepot entity);

    ResultatAction<YvsBaseEmplacementDepot> update(YvsBaseEmplacementDepot entity);

    ResultatAction<YvsBaseEmplacementDepot> delete(YvsBaseEmplacementDepot entity);
}
