/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.com.achat;

import yvs.dao.DaoInterfaceWs;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComContenuDocAchat  extends AYvsComContenuDocAchat implements IYvsComContenuDocAchat{

    public SYvsComContenuDocAchat() {
    }

    public SYvsComContenuDocAchat(DaoInterfaceWs dao) {
        super(dao);
    }
    
}
