/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.produit;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz Dowes
 */
public class SYvsBaseArticles extends AYvsBaseArticles implements IYvsBaseArticles{

    public SYvsBaseArticles() {
    }   

    public SYvsBaseArticles(DaoInterfaceLocal dao) {
        super(dao);
    }
    
}
