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
public class SYvsComCommissionCommerciaux extends AYvsComCommissionCommerciaux implements IYvsComCommissionCommerciaux {

    public SYvsComCommissionCommerciaux() {
    }

    public SYvsComCommissionCommerciaux(DaoInterfaceWs dao) {
        super(dao);
    }

}
