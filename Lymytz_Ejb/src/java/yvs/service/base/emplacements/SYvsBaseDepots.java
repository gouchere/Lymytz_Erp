/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.emplacements;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBaseDepots extends AYvsBaseDepots implements IYvsBaseDepots  {

    public SYvsBaseDepots() {
    }

    public SYvsBaseDepots(DaoInterfaceWs dao) {
        super(dao);
    }

}
