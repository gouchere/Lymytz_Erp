/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.proj;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.proj.projet.YvsProjProjetService;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsProjProjetService {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsProjProjetService> toString(String value);

    ResultatAction<YvsProjProjetService> controle(YvsProjProjetService entity);

    ResultatAction<YvsProjProjetService> save(YvsProjProjetService entity);

    ResultatAction<YvsProjProjetService> update(YvsProjProjetService entity);

    ResultatAction<YvsProjProjetService> delete(YvsProjProjetService entity);
}
