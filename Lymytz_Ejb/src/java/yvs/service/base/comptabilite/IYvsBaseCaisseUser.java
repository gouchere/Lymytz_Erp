/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseCaisseUser;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseCaisseUser {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseCaisseUser> toString(String value);

    ResultatAction<YvsBaseCaisseUser> controle(YvsBaseCaisseUser entity);

    ResultatAction<YvsBaseCaisseUser> save(YvsBaseCaisseUser entity);

    ResultatAction<YvsBaseCaisseUser> update(YvsBaseCaisseUser entity);

    ResultatAction<YvsBaseCaisseUser> delete(YvsBaseCaisseUser entity);
}
