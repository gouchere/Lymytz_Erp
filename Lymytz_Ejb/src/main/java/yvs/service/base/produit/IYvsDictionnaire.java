/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.YvsDictionnaire;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsDictionnaire {
     void setDao(DaoInterfaceLocal dao );

    ResultatAction<YvsDictionnaire> toString(String value);

    ResultatAction<YvsDictionnaire> controle(YvsDictionnaire entity);

    ResultatAction<YvsDictionnaire> save(YvsDictionnaire entity);

    ResultatAction<YvsDictionnaire> update(YvsDictionnaire entity);

    ResultatAction<YvsDictionnaire> delete(YvsDictionnaire entity);
}
