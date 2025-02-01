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
public class SYvsComProformaVenteContenu extends AYvsComProformaVenteContenu implements IYvsComProformaVenteContenu {

    public SYvsComProformaVenteContenu() {
    }

    public SYvsComProformaVenteContenu(DaoInterfaceLocal dao) {
        super(dao);
    }

}
