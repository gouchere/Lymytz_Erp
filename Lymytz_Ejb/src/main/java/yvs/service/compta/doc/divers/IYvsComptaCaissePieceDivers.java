/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaCaissePieceDivers {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaCaissePieceDivers> toString(String value);

    ResultatAction<YvsComptaCaissePieceDivers> controle(YvsComptaCaissePieceDivers entity);

    ResultatAction<YvsComptaCaissePieceDivers> save(YvsComptaCaissePieceDivers entity);

    ResultatAction<YvsComptaCaissePieceDivers> update(YvsComptaCaissePieceDivers entity);

    ResultatAction<YvsComptaCaissePieceDivers> delete(YvsComptaCaissePieceDivers entity);
}
