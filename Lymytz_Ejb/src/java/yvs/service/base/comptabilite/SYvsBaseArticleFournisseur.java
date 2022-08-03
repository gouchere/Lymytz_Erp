/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBaseArticleFournisseur extends AYvsBaseArticleFournisseur implements IYvsBaseArticleFournisseur {

    public SYvsBaseArticleFournisseur() {
    }

    public SYvsBaseArticleFournisseur(DaoInterfaceWs dao) {
        super(dao);
    }
 
}
