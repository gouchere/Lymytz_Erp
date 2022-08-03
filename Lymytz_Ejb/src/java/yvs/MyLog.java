/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lymytz Dowes
 */
public class MyLog {

    Class CLass;
    String methode;
    Date debut = new Date();
    Date fin = new Date();

    static List<MyLog> process = new ArrayList<>();
    static String FILE_SEPARATOR = System.getProperty("file.separator");
    static String path = System.getProperty("user.home") + FILE_SEPARATOR + "lymytz" + FILE_SEPARATOR + "logs";
    static String fileName = "log";
    static String fileExtension = ".txt";

    private MyLog() {

    }

    private MyLog(Class CLass, String methode) {
        this.CLass = CLass;
        this.methode = methode;
    }

    public static void Write(String message) {
        Write(message, false, false);
    }

    public static void Write(String message, boolean time, boolean start) {
        new MyLog().WriteLine(message, time, start);
    }

    public synchronized static void Write(Class CLass, String methode, boolean time, boolean start) {
        try {
            int index = process.indexOf(new MyLog(CLass, methode));
            if (index > -1) {
                process.get(index).WriteLine(CLass.getName() + "(" + methode + ")", time, start);
                if (!start && process.size() > index) {
                    process.remove(index);
                }
            } else {
                MyLog log = new MyLog(CLass, methode);
                log.WriteLine(CLass.getName() + "(" + methode + ")", time, start);
                if (time && start) {
                    process.add(log);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(MyLog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void WriteLine(String message, boolean time, boolean start) {
        try {
            if (time) {
                if (start) {
                    debut = new Date();
                }
                fin = new Date();
            }
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(path + FILE_SEPARATOR + fileName + fileExtension);
            if (!file.exists()) {
                file.createNewFile();
            }
            long fileSize = Files.size(file.toPath());
            long fileSizeKb = fileSize / 1024;
            long fileSizeMb = fileSizeKb / 1024;
            if (fileSizeMb > 1) {
                file.renameTo(new File(path + FILE_SEPARATOR + fileName + "_" + System.currentTimeMillis() + fileExtension));
                file = new File(path + FILE_SEPARATOR + fileName + fileExtension);
                file.createNewFile();
            }
            String oldContent = "";
            BufferedReader reader = new BufferedReader(new FileReader(file));
            //Reading all the lines of input text file into oldContent
            String line = reader.readLine();
            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }
            try (FileWriter writer = new FileWriter(file)) {
                BufferedWriter buffered = new BufferedWriter(writer);
                buffered.write(oldContent);
                buffered.write(new Date().toString() + " : " + message + (time ? " (" + (fin.getTime() - debut.getTime()) + "ms)" : ""));
                buffered.close();
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            System.out.println(new Date().toString() + " : " + message + (time ? " (" + (fin.getTime() - debut.getTime()) + "ms)" : ""));
            Logger.getLogger(MyLog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MyLog other = (MyLog) obj;
        if (!Objects.equals(this.CLass, other.CLass)) {
            return false;
        }
        if (!Objects.equals(this.methode, other.methode)) {
            return false;
        }
        return true;
    }

}
