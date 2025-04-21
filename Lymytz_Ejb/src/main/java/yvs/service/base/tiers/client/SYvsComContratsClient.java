/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers.client;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComContratsClient extends AYvsComContratsClient implements IYvsComContratsClient {

    public SYvsComContratsClient()  {
    }

    public SYvsComContratsClient(DaoInterfaceLocal dao) {
        super(dao);
    }

}
