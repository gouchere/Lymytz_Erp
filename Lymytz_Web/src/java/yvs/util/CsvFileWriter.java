/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lymytz
 */
public class CsvFileWriter implements InterfaceCsvFileWriter {

    private File file;
    private char separator;

    public CsvFileWriter(File file) {
        this(file, ';');
    }

    public CsvFileWriter(File file, char separator) {
        if (file == null) {
            throw new IllegalArgumentException("Le fichier ne peut pas etre nul");
        }
        this.file = file;
        this.separator = separator;
    }

    private void writeEmptyFile() {

    }

    @Override
    public void write(List<Map<String, String>> mappedData) {
        if (mappedData == null) {
            throw new IllegalArgumentException("la liste ne peut pas être nulle");
        }
        if (mappedData.isEmpty()) {
            writeEmptyFile();
        }
        final Map<String, String> oneData = mappedData.get(0);
        final String[] titles = new String[oneData.size()];
        int i = 0;
        for (String key : oneData.keySet()) {
            titles[i++] = key;
        }
        write(mappedData, titles);
    }

    @Override
    public void write(List<Map<String, String>> mappedData, String[] titles) {
        if (mappedData == null) {
            throw new IllegalArgumentException("la liste ne peut pas être nulle");
        }
        if (titles == null) {
            throw new IllegalArgumentException("les titres ne peuvent pas être nuls");
        }
        if (mappedData.isEmpty()) {
            writeEmptyFile();
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            // Les titres
            try {
                boolean first = true;
                for (String title : titles) {
                    if (first) {
                        first = false;
                    } else {
                        try {
                            bw.write(separator);
                        } catch (IOException ex) {
                            Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        write(title, bw);
                    } catch (IOException ex) {
                        Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    bw.write("\n");
                } catch (IOException ex) {
                    Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Les données
                for (Map<String, String> oneData : mappedData) {
                    first = true;
                    for (String title : titles) {
                        if (first) {
                            first = false;
                        } else {
                            try {
                                bw.write(separator);
                            } catch (IOException ex) {
                                Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        final String value = oneData.get(title);
                        try {
                            write(value, bw);
                        } catch (IOException ex) {
                            Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    try {
                        bw.write("\n");
                    } catch (IOException ex) {
                        Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } finally {
                try {
                    bw.close();
                } catch (IOException ex) {
                    Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void write(String value, BufferedWriter bw) throws IOException {
        if (value == null) {
            value = "";
        }
        boolean needQuote = false;
        if (value.indexOf("\n") != -1) {
            needQuote = true;
        }
        if (value.indexOf(separator) != -1) {
            needQuote = true;
        }
        if (value.indexOf("\"") != -1) {
            needQuote = true;
            value = value.replaceAll("\"", "\"\"");
        }
        if (needQuote) {
            value = "\"" + value + "\"";
        }
        bw.write(value);
    }
}
