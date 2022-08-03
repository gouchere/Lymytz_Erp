/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaAcompteClient;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaAcompteClient {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaAcompteClient> toString(String value);

    ResultatAction<YvsComptaAcompteClient> controle(YvsComptaAcompteClient entity);

    ResultatAction<YvsComptaAcompteClient> save(YvsComptaAcompteClient entity);

    ResultatAction<YvsComptaAcompteClient> update(YvsComptaAcompteClient entity);

    ResultatAction<YvsComptaAcompteClient> delete(YvsComptaAcompteClient entity);
}
