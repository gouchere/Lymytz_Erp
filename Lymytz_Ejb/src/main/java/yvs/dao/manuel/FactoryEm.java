/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.manuel;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Lymytz
 */
public class FactoryEm {

    private static EntityManagerFactory emf;
    private static FactoryEm instance;    

    private FactoryEm() {
        emf = Persistence.createEntityManagerFactory("LYMYTZ-ERP-ejbPU");
    }

    public static synchronized FactoryEm getInstance() {
        if (instance == null) {
            instance = new FactoryEm();
        }
        return instance;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }
   
}
