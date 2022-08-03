/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBaseLiaisonCaisse extends AYvsBaseLiaisonCaisse implements IYvsBaseLiaisonCaisse{

    public SYvsBaseLiaisonCaisse() {
    }

    public SYvsBaseLiaisonCaisse(DaoInterfaceWs dao) {
        super(dao);
    }
    
    
}
