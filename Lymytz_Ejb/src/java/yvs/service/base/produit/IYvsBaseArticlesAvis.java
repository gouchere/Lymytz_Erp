/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.produits.YvsBaseArticlesAvis;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticlesAvis {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticlesAvis> toString(String value);

    ResultatAction<YvsBaseArticlesAvis> controle(YvsBaseArticlesAvis entity);

    ResultatAction<YvsBaseArticlesAvis> save(YvsBaseArticlesAvis entity);

    ResultatAction<YvsBaseArticlesAvis> update(YvsBaseArticlesAvis entity);

    ResultatAction<YvsBaseArticlesAvis> delete(YvsBaseArticlesAvis entity);
}
