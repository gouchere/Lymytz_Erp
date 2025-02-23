/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaCaissePieceVente;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaCaissePieceVente {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaCaissePieceVente> toString(String value);

    ResultatAction<YvsComptaCaissePieceVente> controle(YvsComptaCaissePieceVente entity); 

    ResultatAction<YvsComptaCaissePieceVente> save(YvsComptaCaissePieceVente entity);

    ResultatAction<YvsComptaCaissePieceVente> update(YvsComptaCaissePieceVente entity);

    ResultatAction<YvsComptaCaissePieceVente> delete(YvsComptaCaissePieceVente entity);
}
