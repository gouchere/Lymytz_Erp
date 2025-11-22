/*
package yvs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

*/
/**
 *
 * @author lymytz
 *//*


public class Compress {

    //lecture d'un fichier zip
    public static List<String> read(String pathFile) {
        final List<String> list = new ArrayList<>();
        Path jarfile = Paths.get(pathFile);
        Path mf;
        try {
            FileSystem fs = FileSystems.newFileSystem(jarfile, null);
            mf = fs.getPath("META-INF", "MANIFEST.MF");
        } catch (IOException ex) {
            Logger.getLogger(Compress.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        if (mf != null) {
            try (BufferedReader readBuffer = Files.newBufferedReader(mf, Charset.defaultCharset())) {
                String ligne;
                while ((ligne = readBuffer.readLine()) != null) {
                    list.add(ligne);
                }
                return list;
            } catch (IOException ex) {
                Logger.getLogger(Compress.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return null;
    }

    //extraire un fichier zip
    public static boolean extract(String pathSource, String pathCible) {
        Path jarfile = Paths.get(pathSource);
        try {
            FileSystem fs = FileSystems.newFileSystem(jarfile, null);
            // Path du fichier cible
            final Path cible = Paths.get(pathCible);
            Files.deleteIfExists(cible);
            // extraire l'élément de l'archive
            Files.copy(fs.getPath("/META-INF/MANIFEST.MF"), cible);
            return Files.exists(cible);
        } catch (IOException ex) {
            Logger.getLogger(Compress.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //creation d'un fichier zip vide
    public static FileSystem create(String pathFile) {
        Path jarfile = Paths.get(pathFile.replace(" ", "%20"));
        final URI uri = URI.create("jar:file:" + jarfile.toUri().getPath());
        final Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        try {
            return FileSystems.newFileSystem(uri, env);
        } catch (IOException ex) {
            Logger.getLogger(Compress.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //ajouter des fichier a un fichier zip
    public static void addFile(String pathFile, List<File> files) {
        final Path pathZip = Paths.get(pathFile);
        try {
            Files.deleteIfExists(pathZip);
            // important : invoquer la méthode close() du FS
            try (FileSystem fss = create(pathFile)) {
                for (File file : files) {
                    Path source = Paths.get(file.getAbsolutePath());
                    Path dest = fss.getPath("/", file.getName());
                    Files.copy(source, dest, StandardCopyOption.COPY_ATTRIBUTES);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Compress.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
*/
