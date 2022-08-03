/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

/**
 *
 * @author Lymytz Dowes
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileInfo {

    public enum Timefield {

        CREATED, ACCESSED, WRITTEN
    }
    private final static DateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy  hh:mm");
    private File file;
    private boolean hasLoaded = false;
    private String owner;
    private Map<Timefield, Date> timefields = new HashMap<Timefield, Date>();

    public FileInfo(File file) {
        this.file = file;
    }

    private String getTimefieldSwitch(Timefield field) {
        switch (field) {
            case CREATED:
                return "C";
            case ACCESSED:
                return "A";
            default:
                return "W";
        }
    }

    private void shellToDir(Timefield timefield) throws IOException, ParseException {
        Runtime systemShell = Runtime.getRuntime();
        Process output = systemShell.exec(String.format("cmd /c dir /Q /R /T%s %s ", getTimefieldSwitch(timefield), file.getAbsolutePath()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(output.getInputStream()));
        String outputLine = null;
        while ((outputLine = reader.readLine()) != null) {
            if (outputLine.contains(file.getName())) {
                timefields.put(timefield, FORMATTER.parse(outputLine.substring(0, 17)));
                owner = outputLine.substring(36, 59);
            }
        }
    }

    private void load() throws IOException, ParseException {
        if (hasLoaded) {
            return;
        }
        shellToDir(Timefield.CREATED);
        shellToDir(Timefield.ACCESSED);
        shellToDir(Timefield.WRITTEN);
    }

    public String getName() {
        return file.getName();
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public String getCanonicalPath() throws IOException {
        return file.getCanonicalPath();
    }

    public long getSize() {
        return file.length();
    }

    public boolean exists() {
        return file.exists();
    }

    public String getExtension() {
        return Util.getExtension(getName());
    }

    public Date getLastModified() {
        return new Date(file.lastModified());
    }

    public String getOwner() throws IOException, ParseException {
        load();
        return owner;
    }

    public Date getCreated() throws IOException, ParseException {
        load();
        return timefields.get(Timefield.CREATED);
    }

    public Date getAccessed() throws IOException, ParseException {
        load();
        return timefields.get(Timefield.ACCESSED);
    }

    public Date getWritten() throws IOException, ParseException {
        load();
        return timefields.get(Timefield.WRITTEN);
    }
}
