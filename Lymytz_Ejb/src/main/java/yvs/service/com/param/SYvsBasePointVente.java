/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.param;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBasePointVente extends AYvsBasePointVente implements IYvsBasePointVente {

    public SYvsBasePointVente() {
    }

    public SYvsBasePointVente(DaoInterfaceWs dao) {
        super(dao);
    }

}
