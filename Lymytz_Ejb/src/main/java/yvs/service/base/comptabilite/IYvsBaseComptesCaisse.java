/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBaseComptesCaisse;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseComptesCaisse {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseComptesCaisse> toString(String value);

    ResultatAction<YvsBaseComptesCaisse> controle(YvsBaseComptesCaisse entity);

    ResultatAction<YvsBaseComptesCaisse> save(YvsBaseComptesCaisse entity);

    ResultatAction<YvsBaseComptesCaisse> update(YvsBaseComptesCaisse entity);

    ResultatAction<YvsBaseComptesCaisse> delete(YvsBaseComptesCaisse entity);
}
