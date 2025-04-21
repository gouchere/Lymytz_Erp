/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.dashboards.commercial;

import java.util.List;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class DashBoadComImplV1 extends AbstractDashBoadCom implements IDashBoardCom{

    public DashBoadComImplV1(DaoInterfaceLocal dao) {
        super(dao);
    }
        
    @Override
    public void save(){
        System.err.println("----- Save 1");
    }

   
    
}
