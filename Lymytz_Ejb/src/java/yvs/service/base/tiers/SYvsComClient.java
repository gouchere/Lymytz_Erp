/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComClient extends AYvsComClient implements IYvsComClient {

    public SYvsComClient()  {
    }

    public SYvsComClient(DaoInterfaceWs dao) {
        super(dao);
    }

}
