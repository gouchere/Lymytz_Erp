/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBaseArticlePack extends AYvsBaseArticlePack implements IYvsBaseArticlePack{

    public SYvsBaseArticlePack() {
    }

    public SYvsBaseArticlePack(DaoInterfaceLocal dao) {
        super(dao);
    }
    
    
}
