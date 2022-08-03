/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseLiaisonCaisse;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseLiaisonCaisse {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseLiaisonCaisse> toString(String value);

    ResultatAction<YvsBaseLiaisonCaisse> controle(YvsBaseLiaisonCaisse entity);

    ResultatAction<YvsBaseLiaisonCaisse> save(YvsBaseLiaisonCaisse entity);

    ResultatAction<YvsBaseLiaisonCaisse> update(YvsBaseLiaisonCaisse entity);

    ResultatAction<YvsBaseLiaisonCaisse> delete(YvsBaseLiaisonCaisse entity);
}
