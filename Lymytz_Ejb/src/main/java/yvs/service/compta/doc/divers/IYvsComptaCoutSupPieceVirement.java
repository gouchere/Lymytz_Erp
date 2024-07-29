/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaCoutSupPieceVirement;

/**
 *
 * @author hp Elite 8300
 */
public interface IYvsComptaCoutSupPieceVirement {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaCoutSupPieceVirement> toString(String value);

    ResultatAction<YvsComptaCoutSupPieceVirement> controle(YvsComptaCoutSupPieceVirement entity);

    ResultatAction<YvsComptaCoutSupPieceVirement> save(YvsComptaCoutSupPieceVirement entity);

    ResultatAction<YvsComptaCoutSupPieceVirement> update(YvsComptaCoutSupPieceVirement entity);

    ResultatAction<YvsComptaCoutSupPieceVirement> delete(YvsComptaCoutSupPieceVirement entity);

}
