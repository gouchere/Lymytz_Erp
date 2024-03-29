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
public class DaoGenericWs<T extends Serializable> extends AbstractDao<T> implements DaoInterfaceWs<T> {

    String entityName;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public DaoGenericWs() {
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
