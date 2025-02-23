/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.com.vente;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComProformaVenteContenu;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComProformaVenteContenu {
    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComProformaVenteContenu> toString(String value);

    ResultatAction<YvsComProformaVenteContenu> controle(YvsComProformaVenteContenu entity);

    ResultatAction<YvsComProformaVenteContenu> save(YvsComProformaVenteContenu entity);

    ResultatAction<YvsComProformaVenteContenu> update(YvsComProformaVenteContenu entity);

    ResultatAction<YvsComProformaVenteContenu> delete(YvsComProformaVenteContenu entity);
    
}
