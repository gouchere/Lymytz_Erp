/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package yvs.timer.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import yvs.dao.*;
import yvs.entity.param.YvsSocietes;
import yvs.entity.synchro.YvsSynchroTables;
import yvs.init.Chemins;
import yvs.timer.InterfaceTimerLocal;

*/
/**
 *
 * @author LYMYTZ-PC
 *//*

@Singleton
public class TimerDbToXml implements InterfaceTimerLocal {

    @EJB
    public DaoInterfaceLocal dao;
    Chemins chemin = new Chemins();

    public TimerDbToXml() {

    }

    public TimerDbToXml(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    @Schedule(dayOfMonth = "*", month = "*", year = "*", dayOfWeek = "*", hour = "*", minute = "59", persistent = false)
    @Override
    public void myTimer() {
//        createFileXml();
    }

    @Timeout
    public void timeout() {

    }

    @Override
    public void avancement() {
        //-
    }

    public boolean createFileXml() {
//        try {
//            List<YvsSocietes> societes = dao.loadNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
//            for (YvsSocietes societe : societes) {
//                chemin.initRepResource(societe.getCodeAbreviation());
//                List<String> values = buildXmlValues(societe);
//                if (values != null ? !values.isEmpty() : false) {
//                    try {
//                        String localPath = Chemins.getCheminDatabase();
//                        File fileLocal = new File(localPath, "database.xml");
//                        if (fileLocal.exists()) {
//                            long currentMillis = new Date().getTime();
//                            fileLocal.renameTo(new File(localPath, "database_" + currentMillis + ".xml"));
//                        }
//                        fileLocal.createNewFile();
//                        try (FileWriter write = new FileWriter(fileLocal)) {
//                            try (BufferedWriter out = new BufferedWriter(write)) {
//                                out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//                                out.newLine();
//                                out.append("<database>");
//                                for (String ligne : values) {
//                                    out.append(ligne);
//                                }
//                                out.newLine();
//                                out.append("</database>");
//                            }
//                        }
//                        String pathDatabase = localPath.substring(Chemins.getCheminAllDoc().length(), localPath.length());
//                        String remotePath = Chemins.getCheminResource() + Chemins.FILE_SEPARATOR + pathDatabase;
//                        File fileRemote = new File(remotePath, "database.xml");
//                        if (fileRemote.exists()) {
//                            long currentMillis = new Date().getTime();
//                            fileRemote.renameTo(new File(remotePath, "database_" + currentMillis + ".xml"));
//                        }
//                        Util.copyFile("", remotePath, new FileInputStream(fileLocal));
//                    } catch (IOException ex) {
//                        Logger.getLogger(TimerDbToXml.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//            return true;
//        } catch (Exception ex) {
//            Logger.getLogger(TimerDbToXml.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return false;
    }

    public List<String> buildXmlValues(YvsSocietes societe) {
        List<String> result = new ArrayList<>();
        try {
            if (societe != null ? societe.getId() > 0 : false) {
                List<YvsSynchroTables> list = dao.loadNameQueries("YvsSynchroTables.findAll", new String[]{}, new Object[]{});
                if (list != null ? !list.isEmpty() : false) {
                    for (YvsSynchroTables y : list) {
                        String xml = buildXmlValuesTable(societe, y.getTableName(), y.getUseSociete().toString());
                        if (xml != null) {
                            result.add(xml);
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(TimerDbToXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public String buildXmlValuesTable(YvsSocietes societe, String table, String useSociete) {
        try {
            if (asString(table) && (societe != null ? societe.getId() > 0 : false)) {
                List<String> authors = dao.field(table, "author", "yvs_users_agence");
                if (authors != null ? !authors.isEmpty() : false) {
                    String select = "SELECT * FROM " + table + " y";
                    if (asString(useSociete) ? Boolean.valueOf(useSociete) : false) {
                        select += " INNER JOIN yvs_users_agence ua ON y.author = ua.id INNER JOIN yvs_agences a ON ua.agence = a.id WHERE a.societe = " + societe.getId();
                    }
                    System.err.println("select : " + select);
                    String xmls = "table xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
                    String xmls_end = "</table>";
                    Options[] params = new Options[]{new Options(select, 1), new Options(xmls, 2), new Options(table, 3), new Options(xmls_end, 4), new Options("</" + table + ">", 5)};
                    String query = "SELECT REPLACE(REPLACE(XMLSERIALIZE(DOCUMENT query_to_xml(?, false, false, '') AS TEXT), ?, ?), ?, ?)";
                    return (String) dao.loadObjectBySqlQuery(query, params);
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(TimerDbToXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean asString(String valeur) {
        if (valeur != null ? valeur.trim().length() > 0 : false) {
            return true;
        }
        return false;
    }
}
*/
