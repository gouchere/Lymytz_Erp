/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.param;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsSocietesAvis extends AYvsSocietesAvis implements IYvsSocietesAvis{

    public SYvsSocietesAvis() {
    }

    public SYvsSocietesAvis(DaoInterfaceLocal dao) {
        super(dao);
    }    
    
}
