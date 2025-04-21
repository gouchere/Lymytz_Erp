/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBaseFournisseur extends AYvsBaseFournisseur implements IYvsBaseFournisseur {

    public SYvsBaseFournisseur()  {
    }

    public SYvsBaseFournisseur(DaoInterfaceLocal dao) {
        super(dao);
    }

}
