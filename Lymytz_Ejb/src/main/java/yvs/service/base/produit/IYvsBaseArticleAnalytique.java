/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleAnalytique;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticleAnalytique {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticleAnalytique> toString(String value);

    ResultatAction<YvsBaseArticleAnalytique> controle(YvsBaseArticleAnalytique entity);

    ResultatAction<YvsBaseArticleAnalytique> save(YvsBaseArticleAnalytique entity);

    ResultatAction<YvsBaseArticleAnalytique> update(YvsBaseArticleAnalytique entity);

    ResultatAction<YvsBaseArticleAnalytique> delete(YvsBaseArticleAnalytique entity);

}
