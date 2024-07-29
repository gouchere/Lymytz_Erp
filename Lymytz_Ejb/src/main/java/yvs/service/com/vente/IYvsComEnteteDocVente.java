/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsNiveauAcces;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComEnteteDocVente {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComEnteteDocVente> toString(String value);

    ResultatAction<YvsComEnteteDocVente> controle(YvsComEnteteDocVente entity);

    ResultatAction<YvsComEnteteDocVente> save(YvsComEnteteDocVente entity);

    ResultatAction<YvsComEnteteDocVente> update(YvsComEnteteDocVente entity);

    ResultatAction<YvsComEnteteDocVente> delete(YvsComEnteteDocVente entity);

    public void setAgence(YvsAgences agence);

    public void setNiveauAcces(YvsNiveauAcces agence);

}
