/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComptaCaissePieceAchat extends AYvsComptaCaissePieceAchat implements IYvsComptaCaissePieceAchat {

    public SYvsComptaCaissePieceAchat() {
    }

    public SYvsComptaCaissePieceAchat(DaoInterfaceLocal dao) {
        super(dao);
    }

}
