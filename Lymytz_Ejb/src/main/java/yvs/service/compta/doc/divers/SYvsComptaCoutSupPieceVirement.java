/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author hp Elite 8300
 */
public class SYvsComptaCoutSupPieceVirement extends AYvsComptaCoutSupPieceVirement implements IYvsComptaCoutSupPieceVirement{

    public SYvsComptaCoutSupPieceVirement() {
    }

    public SYvsComptaCoutSupPieceVirement(DaoInterfaceLocal dao) {
        super(dao);
    }
    
    
}
