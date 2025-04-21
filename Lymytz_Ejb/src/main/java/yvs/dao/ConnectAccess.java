/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DOWES
 */
public class ConnectAccess {

    private static Connection conn = null;

    //Old Access
    private String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=D:\\\\unit.accdb";
    private String user = "";
    private String pwd = "";

    private ConnectAccess() {
        try {
            //chargement du driver
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            try {
                conn = (Connection) DriverManager.getConnection(url, user, pwd);
            } catch (SQLException ex) {
                Logger.getLogger(ConnectAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private Connection getConnexion() {
        //Chargerment du driver
        try {
            System.out.println("===========================================");
            System.out.println("Tentative de chargement du pilote JDBC ...");
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            //Class.forName("org.gjt.mm.mysql.Driver");
            System.out.println("Starting DB");
            System.out.println("Pilote JDBC charge avec succes.");
            System.out.println("===========================================");
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        //Connexion a la base de donnee
        try {
            System.out.println("===========================================");
            System.out.println("Tentative de connexion a  la base de donnee ");
            conn = (Connection) DriverManager.getConnection(url, user, pwd);
            /*database+= filename.trim() + ";DriverID=22;READONLY=true}";  
             conn = DriverManager.getConnection(database, "",""); */
            System.out.println("Connexion avec succes a  la base de donnee ");
            System.out.println("============================================");
        } catch (SQLException sqlExc) {
            System.out.println("SQLException : " + sqlExc.getMessage());
            System.out.println("SQLState : " + sqlExc.getSQLState());
            System.out.println("VendorError : " + sqlExc.getErrorCode());
        }

        return conn;
    }

    public void getDeconnection(Connection conn) {
        try {
            conn.close();
            System.out.println("Deconnexion effective a  la base de donnee ");
            System.out.println("===========================================");
        } catch (SQLException sqlExc) {
            System.out.println("SQLException : " + sqlExc.getMessage());
            System.out.println("SQLState : " + sqlExc.getSQLState());
            System.out.println("VendorError : " + sqlExc.getErrorCode());
        } finally {
            conn = null;
        }
    }
}
