/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.produits.group.YvsBaseGroupesArticle;

/**
 *
 * @author Lymytz Dowes
 */
public interface IYvsBaseGroupesArticle {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseGroupesArticle> toString(String value);

    ResultatAction<YvsBaseGroupesArticle> controle(YvsBaseGroupesArticle entity);

    ResultatAction<YvsBaseGroupesArticle> save(YvsBaseGroupesArticle entity);

    ResultatAction<YvsBaseGroupesArticle> update(YvsBaseGroupesArticle entity);

    ResultatAction<YvsBaseGroupesArticle> delete(YvsBaseGroupesArticle entity);
}
