/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.param.workflow;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsWorkflowValidFactureVente {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsWorkflowValidFactureVente> toString(String value);

    ResultatAction<YvsWorkflowValidFactureVente> controle(YvsWorkflowValidFactureVente entity);

    ResultatAction<YvsWorkflowValidFactureVente> save(YvsWorkflowValidFactureVente entity);

    ResultatAction<YvsWorkflowValidFactureVente> update(YvsWorkflowValidFactureVente entity);

    ResultatAction<YvsWorkflowValidFactureVente> delete(YvsWorkflowValidFactureVente entity);
}
