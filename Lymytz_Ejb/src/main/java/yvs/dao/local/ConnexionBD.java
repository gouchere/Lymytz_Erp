/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.local;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author LENOVO
 */
public class ConnexionBD {

    private static ConnexionBD instance;  //l'instance doit être unique et non existé pour chaque instance de la classe
    public static EntityManager em = null;
    public static EntityManagerFactory emf = null;
    public static String USER = "postgres";
    public static String PASSWORD = "yves1910/";
    public static String URL;
    public static String HOST = "localhost";
    public static String PORT = "5432";
    public static String DATABASE = "lymytz_demo_0";

    private ConnexionBD() throws Exception {
        URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;
        try {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("javax.persistence.jdbc.url", URL);
            properties.put("javax.persistence.jdbc.user", USER);
            properties.put("javax.persistence.jdbc.password", PASSWORD);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("LymytzSellPU", properties);
            em = emf.createEntityManager();
            this.emf=emf;
        } catch (Exception ex) {
            Logger.getLogger(ConnexionBD.class.getName()).log(Level.SEVERE, "Unnable  to connect", ex);
            throw new Exception("Impossible de se connecter à la source de données !");
        }
    }

    public static ConnexionBD getInstance() throws Exception {
        if (instance == null) {
            try {
                synchronized (ConnexionBD.class) {
                    instance = new ConnexionBD();
                }
            } catch (Exception ex) {
                Logger.getLogger(ConnexionBD.class.getName()).log(Level.SEVERE, null, ex);
                throw new Exception("Impossible de se connecter à la source de données !");
            }
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void setInstance(ConnexionBD instance) {
        ConnexionBD.instance = instance;
    }

    public static Connection getDataBaseCon() {
        try {
            URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(ConnexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
