/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBasePlanComptable;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBasePlanComptable { 

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBasePlanComptable> toString(String value);

    ResultatAction<YvsBasePlanComptable> controle(YvsBasePlanComptable entity);

    ResultatAction<YvsBasePlanComptable> save(YvsBasePlanComptable entity);

    ResultatAction<YvsBasePlanComptable> update(YvsBasePlanComptable entity);

    ResultatAction<YvsBasePlanComptable> delete(YvsBasePlanComptable entity);

}
