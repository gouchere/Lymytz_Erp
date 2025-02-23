/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.emplacements;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBasePointVenteDepot extends AYvsBasePointVenteDepot implements IYvsBasePointVenteDepot{

    public SYvsBasePointVenteDepot() {
    }

    public SYvsBasePointVenteDepot(DaoInterfaceLocal dao) {
        super(dao);
    }
    
    
}
