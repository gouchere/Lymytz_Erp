/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.produits.YvsBaseArticleCodeBarre;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticleCodeBarre {

    void setDao(DaoInterfaceLocal dao );

    ResultatAction<YvsBaseArticleCodeBarre> toString(String value);

    ResultatAction<YvsBaseArticleCodeBarre> controle(YvsBaseArticleCodeBarre entity);

    ResultatAction<YvsBaseArticleCodeBarre> save(YvsBaseArticleCodeBarre entity);

    ResultatAction<YvsBaseArticleCodeBarre> update(YvsBaseArticleCodeBarre entity);

    ResultatAction<YvsBaseArticleCodeBarre> delete(YvsBaseArticleCodeBarre entity);

}
