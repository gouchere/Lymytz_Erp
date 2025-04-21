/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.com.vente;

import yvs.dao.DaoInterfaceLocal;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComCoutSupDocVente extends AYvsComCoutSupDocVente implements IYvsComCoutSupDocVente{

    public SYvsComCoutSupDocVente() {
    }

    public SYvsComCoutSupDocVente(DaoInterfaceLocal dao) {
        super(dao);
    }
    
    
}
