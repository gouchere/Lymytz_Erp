/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.utilisateur;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.users.YvsNiveauAcces;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsNiveauAcces  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsNiveauAcces> toString(String value);

    ResultatAction<YvsNiveauAcces> controle(YvsNiveauAcces entity);

    ResultatAction<YvsNiveauAcces> save(YvsNiveauAcces entity);

    ResultatAction<YvsNiveauAcces> update(YvsNiveauAcces entity);

    ResultatAction<YvsNiveauAcces> delete(YvsNiveauAcces entity);

}
