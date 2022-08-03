/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBasePointVente;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBasePointVente {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBasePointVente> controle(YvsBasePointVente entity);

    ResultatAction<YvsBasePointVente> save(YvsBasePointVente entity);

    ResultatAction<YvsBasePointVente> update(YvsBasePointVente entity);

    ResultatAction<YvsBasePointVente> delete(YvsBasePointVente entity);
}
