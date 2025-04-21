package yvs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lymytz Dowes
 */
public class MyClassLoader {

    public String getParent() {
        return (new File(getPath()).getParent());
    }

    public String getFile() {
        return getClass().getResource("").getFile();
    }

    public String getPath() {
        return getClass().getResource("").getPath();
    }

    public List<Class<?>> getAllClass() {
        List<Class<?>> result = new ArrayList<>();
        try {
            String packageName = "yvs.entity";
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            packageName = packageName.replace(".", "/");
            URL packageURL = classLoader.getResource(packageName);

            if (packageURL.getProtocol().equals("jar")) {
                String jarFileName;
                JarFile jf;
                Enumeration<JarEntry> jarEntries;
                String entryName;

                // build jar file name, then loop through zipped entries
                jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
                jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
                jf = new JarFile(jarFileName);
                jarEntries = jf.entries();
                while (jarEntries.hasMoreElements()) {
                    entryName = jarEntries.nextElement().getName();
                    if (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5) {
                        entryName = entryName.substring(packageName.length(), entryName.lastIndexOf('.'));
                        result.add(getClass(entryName));
                    }
                }

                // loop through files in classpath
            } else {
                URI uri = new URI(packageURL.toString());
                File folder = new File(uri.getPath());
                // won't work with path which contains blank (%20)
                // File folder = new File(packageURL.getFile()); 
                File[] contenuti = folder.listFiles();
                for (File actual : contenuti) {
                    loadSubPackage(packageName, actual, result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Class<?>> findAllClassesUsingClassLoader() {
        List<Class<?>> result = new ArrayList<>();
        try {
            String packageName = "yvs.entity";
            InputStream stream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(packageName.replaceAll("[.]", "/"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".class")) {
                    result.add(getClass(line, packageName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void loadSubPackage(String packageName, File file, List<Class<?>> result) {
        if (file.isDirectory()) {
            File[] contenuti = file.listFiles();
            for (File actual : contenuti) {
                loadSubPackage(packageName, actual, result);
            }
        } else {
            String path = file.getAbsolutePath();
            int index = path.replace("\\", "/").indexOf(packageName);
            String entryName = path.substring(index, path.length()).replace("\\", ".");
            entryName = entryName.substring(0, entryName.lastIndexOf('.'));
            result.add(getClass(entryName));
        }
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    private Class getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}
