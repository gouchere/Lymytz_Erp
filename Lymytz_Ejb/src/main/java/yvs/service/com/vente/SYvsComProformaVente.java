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
public class SYvsComProformaVente extends AYvsComProformaVente implements IYvsComProformaVente{

    public SYvsComProformaVente() {
    }

    public SYvsComProformaVente(DaoInterfaceLocal dao) {
        super(dao);
    }
    
    
}