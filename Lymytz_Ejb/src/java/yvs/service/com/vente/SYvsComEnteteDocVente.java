/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceWs;
import yvs.entity.users.YvsNiveauAcces;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComEnteteDocVente extends AYvsComEnteteDocVente implements IYvsComEnteteDocVente {

    public SYvsComEnteteDocVente() {
    }

    public SYvsComEnteteDocVente(DaoInterfaceLocal dao) {
        super(dao);
    }

}
