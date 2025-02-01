/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComContenuDocAchat {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComContenuDocAchat> toString(String value);

    ResultatAction<YvsComContenuDocAchat> controle(YvsComContenuDocAchat entity);

    ResultatAction<YvsComContenuDocAchat> save(YvsComContenuDocAchat entity);

    ResultatAction<YvsComContenuDocAchat> update(YvsComContenuDocAchat entity);

    ResultatAction<YvsComContenuDocAchat> delete(YvsComContenuDocAchat entity);

    ResultatAction<YvsComContenuDocAchat> returnQuantiteLivre(YvsComContenuDocAchat contenu);
}
