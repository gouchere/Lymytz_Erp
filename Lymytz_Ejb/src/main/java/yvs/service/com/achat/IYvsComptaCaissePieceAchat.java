/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaCaissePieceAchat;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaCaissePieceAchat {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaCaissePieceAchat> toString(String value);

    ResultatAction<YvsComptaCaissePieceAchat> controle(YvsComptaCaissePieceAchat entity);

    ResultatAction<YvsComptaCaissePieceAchat> save(YvsComptaCaissePieceAchat entity);

    ResultatAction<YvsComptaCaissePieceAchat> update(YvsComptaCaissePieceAchat entity);

    ResultatAction<YvsComptaCaissePieceAchat> delete(YvsComptaCaissePieceAchat entity);
}
