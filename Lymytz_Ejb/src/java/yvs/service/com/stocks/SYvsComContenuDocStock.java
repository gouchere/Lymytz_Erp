/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.stocks;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComContenuDocStock extends AYvsComContenuDocStock implements IYvsComContenuDocStock {

    public SYvsComContenuDocStock() {
    }

    public SYvsComContenuDocStock(DaoInterfaceWs dao) {
        super(dao);
    }

}
