/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.sql.DataSource;

/**
 *
 * @author Lymytz Dowes
 */
public class Dao {

    private static Dao INSTANCE;  //l'instance doit être unique et non existé pour chaque instance de la classe
    public static EntityManager EM = null;
    private static Connection CONNECTION;
    static String DATASOURCE_CONTEXT = "jdbc/lymytz-erp";

    private Dao() throws Exception {
        try {
            EM = Persistence.createEntityManagerFactory("LYMYTZ-ERP-ejbPU").createEntityManager();
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Dao getInstance() throws Exception {
        if (INSTANCE == null) {
            try {
                INSTANCE = new Dao();
            } catch (Exception ex) {
                Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
                throw new Exception("Impossible de se connecter à la source de données !");
            }
        }
        return INSTANCE;
    }

    public static Connection CONNECTION() {
        if (CONNECTION == null) {
            try {
                Context initialContext = new InitialContext();
                DataSource ds = (DataSource) initialContext.lookup(DATASOURCE_CONTEXT);
                if (ds != null) {
                    CONNECTION = ds.getConnection();
                }
            } catch (NamingException | SQLException ex) {
                Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return CONNECTION;
    }

    public EntityManager getEntityManager() {
        return EM;
    }
}
