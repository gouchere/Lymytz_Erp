/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.param;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsSocietesAvis extends AYvsSocietesAvis implements IYvsSocietesAvis{

    public SYvsSocietesAvis() {
    }

    public SYvsSocietesAvis(DaoInterfaceWs dao) {
        super(dao);
    }    
    
}
