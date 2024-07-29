/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.param.workflow;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsWorkflowValidDocStock extends AYvsWorkflowValidDocStock implements IYvsWorkflowValidDocStock{

    public SYvsWorkflowValidDocStock() {
    }

    public SYvsWorkflowValidDocStock(DaoInterfaceWs dao) {
        super(dao);
    }
    
    
}
