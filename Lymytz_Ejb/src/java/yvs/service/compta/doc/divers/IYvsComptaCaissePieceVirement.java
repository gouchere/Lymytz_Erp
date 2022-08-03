/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaCaissePieceVirement;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaCaissePieceVirement {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaCaissePieceVirement> toString(String value);

    ResultatAction<YvsComptaCaissePieceVirement> controle(YvsComptaCaissePieceVirement entity);

    ResultatAction<YvsComptaCaissePieceVirement> save(YvsComptaCaissePieceVirement entity);

    ResultatAction<YvsComptaCaissePieceVirement> update(YvsComptaCaissePieceVirement entity);

    ResultatAction<YvsComptaCaissePieceVirement> delete(YvsComptaCaissePieceVirement entity);

}
