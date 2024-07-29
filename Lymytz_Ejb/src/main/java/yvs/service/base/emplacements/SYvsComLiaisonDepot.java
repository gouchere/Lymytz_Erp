/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.emplacements;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComLiaisonDepot extends AYvsComLiaisonDepot implements IYvsComLiaisonDepot{

    public SYvsComLiaisonDepot() {
    }

    public SYvsComLiaisonDepot(DaoInterfaceWs dao) {
        super(dao);
    }
    
    
}
