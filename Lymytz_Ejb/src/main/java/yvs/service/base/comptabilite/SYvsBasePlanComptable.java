/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBasePlanComptable extends AYvsBasePlanComptable implements IYvsBasePlanComptable {

    public SYvsBasePlanComptable() {
    }

    public SYvsBasePlanComptable(DaoInterfaceLocal dao) {
        super(dao);
    }
 
}
