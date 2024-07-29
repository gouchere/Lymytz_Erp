/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseCaisse;


/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseCaisse {
    
     void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseCaisse> toString(String value);

    ResultatAction<YvsBaseCaisse> controle(YvsBaseCaisse entity);

    ResultatAction<YvsBaseCaisse> save(YvsBaseCaisse entity);

    ResultatAction<YvsBaseCaisse> update(YvsBaseCaisse entity);

    ResultatAction<YvsBaseCaisse> delete(YvsBaseCaisse entity);
    
}
