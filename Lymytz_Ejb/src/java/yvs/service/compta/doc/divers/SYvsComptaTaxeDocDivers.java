/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceWs;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComptaTaxeDocDivers extends AYvsComptaTaxeDocDivers implements IYvsComptaTaxeDocDivers{

    public SYvsComptaTaxeDocDivers() {
    }

    public SYvsComptaTaxeDocDivers(DaoInterfaceWs dao) {
        super(dao);
    }
     
    
}
