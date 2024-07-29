/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComTaxeContenuAchat extends AYvsComTaxeContenuAchat implements IYvsComTaxeContenuAchat {

    public SYvsComTaxeContenuAchat() {
    }

    public SYvsComTaxeContenuAchat(DaoInterfaceWs dao) {
        super(dao);
    }

}
