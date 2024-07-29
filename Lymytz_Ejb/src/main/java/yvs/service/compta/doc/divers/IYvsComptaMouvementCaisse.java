/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaMouvementCaisse;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaMouvementCaisse {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaMouvementCaisse> toString(String value);

    ResultatAction<YvsComptaMouvementCaisse> controle(YvsComptaMouvementCaisse entity);

    ResultatAction<YvsComptaMouvementCaisse> save(YvsComptaMouvementCaisse entity);

    ResultatAction<YvsComptaMouvementCaisse> update(YvsComptaMouvementCaisse entity);

    ResultatAction<YvsComptaMouvementCaisse> delete(YvsComptaMouvementCaisse entity);
}
