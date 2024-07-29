/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.produit;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsBaseArticleEquivalent extends AYvsBaseArticleEquivalent implements IYvsBaseArticleEquivalent{

    public SYvsBaseArticleEquivalent() {
    }

    public SYvsBaseArticleEquivalent(DaoInterfaceWs dao) {
        super(dao);
    }
    
    
}
