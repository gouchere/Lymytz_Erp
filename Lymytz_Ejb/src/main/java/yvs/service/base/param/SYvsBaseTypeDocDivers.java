/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBaseTypeDocDivers extends AYvsBaseTypeDocDivers implements IYvsBaseTypeDocDivers {

    public SYvsBaseTypeDocDivers() {
    }

    public SYvsBaseTypeDocDivers(DaoInterfaceWs dao) {
        super(dao);
    }

}
