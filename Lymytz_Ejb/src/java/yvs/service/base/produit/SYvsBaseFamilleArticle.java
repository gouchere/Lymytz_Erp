/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.produit;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz Dowes
 */
public class SYvsBaseFamilleArticle extends AYvsBaseFamilleArticle implements IYvsBaseFamilleArticle{

    public SYvsBaseFamilleArticle() {
    }   

    public SYvsBaseFamilleArticle(DaoInterfaceWs dao) {
        super(dao);
    }
    
}
