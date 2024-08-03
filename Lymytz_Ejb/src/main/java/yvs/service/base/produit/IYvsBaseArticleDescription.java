/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.produits.YvsBaseArticleDescription;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticleDescription {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticleDescription> toString(String value);

    ResultatAction<YvsBaseArticleDescription> controle(YvsBaseArticleDescription entity);

    ResultatAction<YvsBaseArticleDescription> save(YvsBaseArticleDescription entity);

    ResultatAction<YvsBaseArticleDescription> update(YvsBaseArticleDescription entity);

    ResultatAction<YvsBaseArticleDescription> delete(YvsBaseArticleDescription entity);
}
