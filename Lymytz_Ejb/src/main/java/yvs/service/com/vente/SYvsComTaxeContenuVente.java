/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.com.vente;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComTaxeContenuVente extends AYvsComTaxeContenuVente implements IYvsComTaxeContenuVente{

    public SYvsComTaxeContenuVente() {
    }

    public SYvsComTaxeContenuVente(DaoInterfaceLocal dao) {
        super(dao);
    }
    
    
}
