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
public class SYvsBaseArticleSubstitution extends AYvsBaseArticleSubstitution implements IYvsBaseArticleSubstitution {

    public SYvsBaseArticleSubstitution() {
    }

    public SYvsBaseArticleSubstitution(DaoInterfaceLocal dao) {
        super(dao);
    }

}
