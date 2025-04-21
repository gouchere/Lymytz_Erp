/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.analytique.YvsComptaPlanAnalytique;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaPlanAnalytique {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaPlanAnalytique> toString(String value);

    ResultatAction<YvsComptaPlanAnalytique> controle(YvsComptaPlanAnalytique entity);

    ResultatAction<YvsComptaPlanAnalytique> save(YvsComptaPlanAnalytique entity);

    ResultatAction<YvsComptaPlanAnalytique> update(YvsComptaPlanAnalytique entity);

    ResultatAction<YvsComptaPlanAnalytique> delete(YvsComptaPlanAnalytique entity);
}
