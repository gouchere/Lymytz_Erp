/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.param.workflow;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsWorkflowValidFactureAchat {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsWorkflowValidFactureAchat> toString(String value);

    ResultatAction<YvsWorkflowValidFactureAchat> controle(YvsWorkflowValidFactureAchat entity);

    ResultatAction<YvsWorkflowValidFactureAchat> save(YvsWorkflowValidFactureAchat entity);

    ResultatAction<YvsWorkflowValidFactureAchat> update(YvsWorkflowValidFactureAchat entity);

    ResultatAction<YvsWorkflowValidFactureAchat> delete(YvsWorkflowValidFactureAchat entity);
}
