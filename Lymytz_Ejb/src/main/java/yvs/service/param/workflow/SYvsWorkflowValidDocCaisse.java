/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.param.workflow;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsWorkflowValidDocCaisse extends AYvsWorkflowValidDocCaisse implements IYvsWorkflowValidDocCaisse{

    public SYvsWorkflowValidDocCaisse() {
    }

    public SYvsWorkflowValidDocCaisse(DaoInterfaceLocal dao) {
        super(dao);
    }
    
    
}
