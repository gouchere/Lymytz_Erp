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
public class SYvsBaseEmplacementDepot extends AYvsBaseEmplacementDepot implements IYvsBaseEmplacementDepot{

    public SYvsBaseEmplacementDepot() {
    }

    public SYvsBaseEmplacementDepot(DaoInterfaceWs dao) {
        super(dao);
    }
    
    
}
