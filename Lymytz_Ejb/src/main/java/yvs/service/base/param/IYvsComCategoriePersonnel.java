/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.YvsComCategoriePersonnel;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComCategoriePersonnel {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComCategoriePersonnel> toString(String value);

    ResultatAction<YvsComCategoriePersonnel> controle(YvsComCategoriePersonnel entity);

    ResultatAction<YvsComCategoriePersonnel> save(YvsComCategoriePersonnel entity);

    ResultatAction<YvsComCategoriePersonnel> update(YvsComCategoriePersonnel entity);

    ResultatAction<YvsComCategoriePersonnel> delete(YvsComCategoriePersonnel entity);
}
