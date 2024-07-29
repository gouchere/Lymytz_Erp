/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.param;

import yvs.dao.DaoInterfaceWs;
import yvs.entity.compta.YvsBaseNatureCompte;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBaseNatureCompte extends AYvsBaseNatureCompte implements IYvsBaseNatureCompte{

    public SYvsBaseNatureCompte() {
    }

    public SYvsBaseNatureCompte(DaoInterfaceWs dao) {
        super(dao);
    }
    
    
}
