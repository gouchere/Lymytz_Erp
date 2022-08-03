/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaBielletage;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaBielletage {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaBielletage> toString(String value);

    ResultatAction<YvsComptaBielletage> controle(YvsComptaBielletage entity);

    ResultatAction<YvsComptaBielletage> save(YvsComptaBielletage entity);

    ResultatAction<YvsComptaBielletage> update(YvsComptaBielletage entity);

    ResultatAction<YvsComptaBielletage> delete(YvsComptaBielletage entity);
}
