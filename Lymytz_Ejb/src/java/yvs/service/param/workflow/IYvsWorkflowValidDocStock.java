/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.param.workflow;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsWorkflowValidDocStock {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsWorkflowValidDocStock> toString(String value);

    ResultatAction<YvsWorkflowValidDocStock> controle(YvsWorkflowValidDocStock entity);

    ResultatAction<YvsWorkflowValidDocStock> save(YvsWorkflowValidDocStock entity);

    ResultatAction<YvsWorkflowValidDocStock> update(YvsWorkflowValidDocStock entity);

    ResultatAction<YvsWorkflowValidDocStock> delete(YvsWorkflowValidDocStock entity);


}
