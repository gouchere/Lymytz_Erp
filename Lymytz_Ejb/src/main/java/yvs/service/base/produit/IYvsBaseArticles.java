/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsNiveauAcces;

/**
 *
 * @author Lymytz Dowes
 */
public interface IYvsBaseArticles {

    void setDao(DaoInterfaceLocal dao);
     public void setAgence(YvsAgences agence);

    public void setNiveauAcces(YvsNiveauAcces agence);

    ResultatAction<YvsBaseArticles> toString(String value);

    ResultatAction<YvsBaseArticles> controle(YvsBaseArticles entity);

    ResultatAction<YvsBaseArticles> save(YvsBaseArticles entity);

    ResultatAction<YvsBaseArticles> update(YvsBaseArticles entity);

    ResultatAction<YvsBaseArticles> delete(YvsBaseArticles entity);
}
