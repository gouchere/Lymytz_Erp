/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.com.vente;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComProformaVente;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComProformaVente {
    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComProformaVente> toString(String value);

    ResultatAction<YvsComProformaVente> controle(YvsComProformaVente entity);

    ResultatAction<YvsComProformaVente> save(YvsComProformaVente entity);

    ResultatAction<YvsComProformaVente> update(YvsComProformaVente entity);

    ResultatAction<YvsComProformaVente> delete(YvsComProformaVente entity);
    
}
