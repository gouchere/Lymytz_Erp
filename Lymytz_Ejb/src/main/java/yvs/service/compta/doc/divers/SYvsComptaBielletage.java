/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComptaBielletage extends AYvsComptaBielletage implements IYvsComptaBielletage {

    public SYvsComptaBielletage() {
    }

    public SYvsComptaBielletage(DaoInterfaceLocal dao) {
        super(dao);
    }

}
