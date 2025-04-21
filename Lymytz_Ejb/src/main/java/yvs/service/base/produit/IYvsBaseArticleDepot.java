/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleDepot;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticleDepot {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticleDepot> toString(String value);

    ResultatAction<YvsBaseArticleDepot> controle(YvsBaseArticleDepot entity);

    ResultatAction<YvsBaseArticleDepot> save(YvsBaseArticleDepot entity);

    ResultatAction<YvsBaseArticleDepot> update(YvsBaseArticleDepot entity);

    ResultatAction<YvsBaseArticleDepot> delete(YvsBaseArticleDepot entity);
}
