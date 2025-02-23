/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author GOUCHERE YVES give the session factory with jpa entityManager
 */
public class Factory {

    private static Factory instance;
    private static EntityManagerFactory emf;
    private static Object objSynchrone__;

    private Factory() {
        emf = Persistence.createEntityManagerFactory("LYMYTZ-ERP-ejbPU");
    }

    public static synchronized Factory getFactory() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }
}
