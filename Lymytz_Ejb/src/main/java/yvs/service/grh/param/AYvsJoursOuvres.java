/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.grh.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz Dowes
 */
public class AYvsJoursOuvres extends AbstractEntity  {
    
    public AYvsJoursOuvres() {
    }

    public AYvsJoursOuvres(DaoInterfaceLocal dao) {
        this.dao = dao;
    }
}
