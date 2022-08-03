/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleSubstitution;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticleSubstitution {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticleSubstitution> toString(String value);

    ResultatAction<YvsBaseArticleSubstitution> controle(YvsBaseArticleSubstitution entity);

    ResultatAction<YvsBaseArticleSubstitution> save(YvsBaseArticleSubstitution entity);

    ResultatAction<YvsBaseArticleSubstitution> update(YvsBaseArticleSubstitution entity);

    ResultatAction<YvsBaseArticleSubstitution> delete(YvsBaseArticleSubstitution entity);

}
