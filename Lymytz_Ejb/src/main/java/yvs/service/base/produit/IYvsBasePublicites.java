/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.produits.YvsBasePublicites;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBasePublicites {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBasePublicites> toString(String value);

    ResultatAction<YvsBasePublicites> controle(YvsBasePublicites entity);

    ResultatAction<YvsBasePublicites> save(YvsBasePublicites entity);

    ResultatAction<YvsBasePublicites> update(YvsBasePublicites entity);

    ResultatAction<YvsBasePublicites> delete(YvsBasePublicites entity);
}
