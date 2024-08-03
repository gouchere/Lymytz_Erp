/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.achat.YvsComTaxeContenuAchat;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComTaxeContenuAchat {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComTaxeContenuAchat> toString(String value);

    ResultatAction<YvsComTaxeContenuAchat> controle(YvsComTaxeContenuAchat entity);

    ResultatAction<YvsComTaxeContenuAchat> save(YvsComTaxeContenuAchat entity);

    ResultatAction<YvsComTaxeContenuAchat> update(YvsComTaxeContenuAchat entity);

    ResultatAction<YvsComTaxeContenuAchat> delete(YvsComTaxeContenuAchat entity);
}
