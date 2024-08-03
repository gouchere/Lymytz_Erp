/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.com.vente;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComTaxeContenuVente;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComTaxeContenuVente {
    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComTaxeContenuVente> toString(String value);

    ResultatAction<YvsComTaxeContenuVente> controle(YvsComTaxeContenuVente entity);

    ResultatAction<YvsComTaxeContenuVente> save(YvsComTaxeContenuVente entity);

    ResultatAction<YvsComTaxeContenuVente> update(YvsComTaxeContenuVente entity);

    ResultatAction<YvsComTaxeContenuVente> delete(YvsComTaxeContenuVente entity);
    
}
