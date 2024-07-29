/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.utilisateur;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsNiveauAcces extends AYvsNiveauAcces implements IYvsNiveauAcces {

    public SYvsNiveauAcces() {
    }

    public SYvsNiveauAcces(DaoInterfaceWs dao) {
        super(dao);
    }
    
    
}
