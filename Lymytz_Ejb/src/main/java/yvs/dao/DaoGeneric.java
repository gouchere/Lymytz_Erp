/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 *
 */
@Stateless
public class DaoGeneric<T extends Serializable> extends AbstractDao<T> implements DaoInterfaceLocal<T> {

    public DaoGeneric() {
    }

    @Override 
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public EntityManager getEntityManager(String EM) {
        if (null != EM) {
            switch (EM) {
                case "ERP":
                    return em;
            }
        }
        return em;
    }


}
