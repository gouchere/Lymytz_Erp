/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.proj;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsProjProjet extends AYvsProjProjet implements IYvsProjProjet {

    public SYvsProjProjet() {
    }

    public SYvsProjProjet(DaoInterfaceWs dao) {
        super(dao);
    }

}
