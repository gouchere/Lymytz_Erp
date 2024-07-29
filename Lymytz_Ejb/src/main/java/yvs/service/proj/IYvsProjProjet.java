/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.proj;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.proj.projet.YvsProjProjet;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsProjProjet {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsProjProjet> toString(String value);

    ResultatAction<YvsProjProjet> controle(YvsProjProjet entity);

    ResultatAction<YvsProjProjet> save(YvsProjProjet entity);

    ResultatAction<YvsProjProjet> update(YvsProjProjet entity);

    ResultatAction<YvsProjProjet> delete(YvsProjProjet entity);
}
