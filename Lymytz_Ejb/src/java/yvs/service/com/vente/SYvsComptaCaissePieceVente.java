/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.com.vente;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComptaCaissePieceVente extends AYvsComptaCaissePieceVente implements IYvsComptaCaissePieceVente{

    public SYvsComptaCaissePieceVente() {
    }

    public SYvsComptaCaissePieceVente(DaoInterfaceWs dao) {
        super(dao);
    }
    
    
}
