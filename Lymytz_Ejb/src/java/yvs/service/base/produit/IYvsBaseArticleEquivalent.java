/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleEquivalent;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticleEquivalent {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticleEquivalent> toString(String value);

    ResultatAction<YvsBaseArticleEquivalent> controle(YvsBaseArticleEquivalent entity);

    ResultatAction<YvsBaseArticleEquivalent> save(YvsBaseArticleEquivalent entity);

    ResultatAction<YvsBaseArticleEquivalent> update(YvsBaseArticleEquivalent entity);

    ResultatAction<YvsBaseArticleEquivalent> delete(YvsBaseArticleEquivalent entity);

}
