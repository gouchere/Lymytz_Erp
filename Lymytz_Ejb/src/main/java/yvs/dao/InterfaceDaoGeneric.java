/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import yvs.dao.salaire.service.ResultatAction;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 */
public abstract class InterfaceDaoGeneric<T extends Serializable> {

    public  ResultatAction checkEntity(T entity){
        return new ResultatAction();
    };

    public abstract void save(T entity);

    public abstract ResultatAction save1(T entity);

    public abstract T update(T entity);

    public abstract T getOne(Object key);

    public abstract boolean delete(T entity);
    //charge toutes les lignes d'une table sans aucune restriction
}
