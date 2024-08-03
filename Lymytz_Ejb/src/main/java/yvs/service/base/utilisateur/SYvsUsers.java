/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.utilisateur;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsUsers extends AYvsUsers implements IYvsUsers  {

    public SYvsUsers() {
    }

    public SYvsUsers(DaoInterfaceLocal dao) {
        super(dao);
    }

}
