/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.param.workflow;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsWorkflowValidDocCaisse {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsWorkflowValidDocCaisse> toString(String value);

    ResultatAction<YvsWorkflowValidDocCaisse> controle(YvsWorkflowValidDocCaisse entity);

    ResultatAction<YvsWorkflowValidDocCaisse> save(YvsWorkflowValidDocCaisse entity);

    ResultatAction<YvsWorkflowValidDocCaisse> update(YvsWorkflowValidDocCaisse entity);

    ResultatAction<YvsWorkflowValidDocCaisse> delete(YvsWorkflowValidDocCaisse entity);

}
