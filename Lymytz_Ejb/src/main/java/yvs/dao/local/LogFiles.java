/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.local;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class LogFiles {

    private static final long LIMIT = 1024;
    private static final long SIZE = 500;

    public LogFiles() {
    }

    public static boolean createLogfile() {
        File logFile = new File("log");
        File logFile_ = new File("conf");
//            final FileWriter file=new FileWriter(new File("log/appslog.txt"));
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        if (!logFile_.exists()) {
            logFile_.mkdirs();
        }
        logFile = new File("log/appslog.txt");
        logFile_ = new File("conf/serv.ltz");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!logFile_.exists()) {
            try {
                logFile_.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return logFile.exists() && logFile_.exists();
    }

    public static boolean addLogInFile(Exception ex) {
        try {
            File f = new File("log/appslog.txt");
            if(!f.exists()){
                createLogfile();
            }
            final FileWriter fw = new FileWriter(f, true);
            try (PrintWriter pw = new PrintWriter(fw, true)) {
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                pw.write(df.format(new Date()));
                pw.println();
                ex.printStackTrace(pw);
            }
        } catch (IOException ex1) {
            Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, ex1);
        } finally {

        }
        return true;
    }

    public static boolean addLogInFile(String message) {
        try {
            File f = new File("log/appslog.txt");
//            System.err.println(" Taille du fichier " + f.length());
//            if ((f.length() / LIMIT) > SIZE) {
//                System.err.println(" rename file");
//                f.renameTo(new File("log/appslog".concat("_").concat(AllConstantes.dfH.format(new Date())).concat(".txt")));
//                createLogfile();
//            }
            final FileWriter fw = new FileWriter(f, true);
            try (PrintWriter pw = new PrintWriter(fw, true)) {
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                pw.write(df.format(new Date()));
                pw.println();
                pw.write(message);
            }
        } catch (IOException ex1) {
            Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, ex1);
        } finally {

        }
        return true;
    }
}
