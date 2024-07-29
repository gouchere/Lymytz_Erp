/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseFournisseur;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseFournisseur  {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseFournisseur> toString(String value);

    ResultatAction<YvsBaseFournisseur> controle(YvsBaseFournisseur entity);

    ResultatAction<YvsBaseFournisseur> save(YvsBaseFournisseur entity);

    ResultatAction<YvsBaseFournisseur> update(YvsBaseFournisseur entity);

    ResultatAction<YvsBaseFournisseur> delete(YvsBaseFournisseur entity);

}
