/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaJournaux;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaJournaux {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaJournaux> toString(String value);

    ResultatAction<YvsComptaJournaux> controle(YvsComptaJournaux entity);

    ResultatAction<YvsComptaJournaux> save(YvsComptaJournaux entity);

    ResultatAction<YvsComptaJournaux> update(YvsComptaJournaux entity);

    ResultatAction<YvsComptaJournaux> delete(YvsComptaJournaux entity);

}
