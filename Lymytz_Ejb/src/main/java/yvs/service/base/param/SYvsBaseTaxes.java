/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBaseTaxes extends AYvsBaseTaxes implements IYvsBaseTaxes{

    public SYvsBaseTaxes() {
    }

    public SYvsBaseTaxes(DaoInterfaceLocal dao) {
        super(dao);
    }
    
    
}
