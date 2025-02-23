/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.YvsSocietesAvis;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsSocietesAvis {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsSocietesAvis> toString(String value);

    ResultatAction<YvsSocietesAvis> controle(YvsSocietesAvis entity);

    ResultatAction<YvsSocietesAvis> save(YvsSocietesAvis entity);

    ResultatAction<YvsSocietesAvis> update(YvsSocietesAvis entity);

    ResultatAction<YvsSocietesAvis> delete(YvsSocietesAvis entity);
}
