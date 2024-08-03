/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author GOUCHERE YVES
 */
public class FileUtils {

    public static void copy(final InputStream inStream, final OutputStream outStream, final int bufferSize) throws IOException {
        final byte[] buffer = new byte[bufferSize];
        int nbRead;
        while ((nbRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, nbRead);
        }
    }

    public static void copyDirectory(final File from, final File to) throws IOException {
        
        System.err.println("Copy Directory "+to.getAbsolutePath());
        if (!to.exists()) {
            to.mkdir();
        }
        final File[] inDir = from.listFiles();
        for (File file : inDir) {
            copy(file, new File(to, file.getName()));
        }
    }

    public static void copyFile(final File from, final File to) throws IOException {
        final OutputStream outStream;
        System.err.println("Copy File "+to.getAbsolutePath());
        try (InputStream inStream = new FileInputStream(from)) {
            outStream = new FileOutputStream(to);
            copy(inStream, outStream, (int) Math.min(from.length(), 4 * 1024));
        }
        outStream.close();
    }

    public static void copy(final File from, final File to) throws IOException {
        System.err.println("Copy "+to.getAbsolutePath());
        if (from.isFile()) {
            copyFile(from, to);
        } else if (from.isDirectory()) {
            copyDirectory(from, to);
        } else {
            throw new FileNotFoundException(from.toString() + " does not exist");
        }
    }
}
