/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.proj;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.proj.YvsProjDepartement;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsProjDepartement {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsProjDepartement> toString(String value);

    ResultatAction<YvsProjDepartement> controle(YvsProjDepartement entity);

    ResultatAction<YvsProjDepartement> save(YvsProjDepartement entity);

    ResultatAction<YvsProjDepartement> update(YvsProjDepartement entity);

    ResultatAction<YvsProjDepartement> delete(YvsProjDepartement entity);
}
