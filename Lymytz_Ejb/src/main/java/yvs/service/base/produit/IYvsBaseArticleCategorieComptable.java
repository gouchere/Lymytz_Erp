/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleCategorieComptable;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticleCategorieComptable {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticleCategorieComptable> toString(String value);

    ResultatAction<YvsBaseArticleCategorieComptable> controle(YvsBaseArticleCategorieComptable entity);

    ResultatAction<YvsBaseArticleCategorieComptable> save(YvsBaseArticleCategorieComptable entity);

    ResultatAction<YvsBaseArticleCategorieComptable> update(YvsBaseArticleCategorieComptable entity);

    ResultatAction<YvsBaseArticleCategorieComptable> delete(YvsBaseArticleCategorieComptable entity);
}
