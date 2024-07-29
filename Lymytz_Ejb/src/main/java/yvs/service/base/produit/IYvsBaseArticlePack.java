/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.produits.YvsBaseArticleDescription;
import yvs.entity.produits.YvsBaseArticlePack;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticlePack {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticlePack> toString(String value);

    ResultatAction<YvsBaseArticlePack> controle(YvsBaseArticlePack entity);

    ResultatAction<YvsBaseArticlePack> save(YvsBaseArticlePack entity);

    ResultatAction<YvsBaseArticlePack> update(YvsBaseArticlePack entity);

    ResultatAction<YvsBaseArticlePack> delete(YvsBaseArticlePack entity);
}
