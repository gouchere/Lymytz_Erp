/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.produits.group.YvsBaseFamilleArticle;

/**
 *
 * @author Lymytz Dowes
 */
public interface IYvsBaseFamilleArticle {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseFamilleArticle> toString(String value);

    ResultatAction<YvsBaseFamilleArticle> controle(YvsBaseFamilleArticle entity);

    ResultatAction<YvsBaseFamilleArticle> save(YvsBaseFamilleArticle entity);

    ResultatAction<YvsBaseFamilleArticle> update(YvsBaseFamilleArticle entity);

    ResultatAction<YvsBaseFamilleArticle> delete(YvsBaseFamilleArticle entity);
}
