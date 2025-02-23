/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleFournisseur;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticleFournisseur {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticleFournisseur> toString(String value);

    ResultatAction<YvsBaseArticleFournisseur> controle(YvsBaseArticleFournisseur entity);

    ResultatAction<YvsBaseArticleFournisseur> save(YvsBaseArticleFournisseur entity);

    ResultatAction<YvsBaseArticleFournisseur> update(YvsBaseArticleFournisseur entity);

    ResultatAction<YvsBaseArticleFournisseur> delete(YvsBaseArticleFournisseur entity); 
}
