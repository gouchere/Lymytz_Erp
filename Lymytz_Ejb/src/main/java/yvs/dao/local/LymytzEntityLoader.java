/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.local;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Table;
import yvs.entity.YvsEntityLoader;
import yvs.entity.synchro.YvsSynchroListenTable;

/**
 *
 * @author LENOVO
 * @param <T>
 */
public class LymytzEntityLoader<T extends Serializable> {

    List<T> datas;
    String path = "";
    public static List<LymytzEntity> FILESENTITY = new ArrayList<>();

    public LymytzEntityLoader() {
        chemin = new ArrayList<>();
        System.err.println("Début du chargement de la classe builder...");
        URL source = YvsEntityLoader.class.getResource("");
        path = new File(source.getFile()).getName();
        System.err.println("File : "+path);
        System.err.println("File : "+source.getFile());
        findAllClasse(new File(source.getFile()));        
        System.err.println("Fin du chargement de la classe builder...");
    }

    public void findAllClasse(File dossier) {
        StringBuilder sb;
        if (dossier != null) {
            String parent;
            String temp;
            String annotation;
            for (File f : dossier.listFiles()) {
                sb = new StringBuilder();
                parent = getSubPackage(f);
                if (f.isDirectory()) {
                    findAllClasse(f);
                } else {
                    if (f.getName().endsWith(".class")) {
                        sb.append("yvs.").append(parent).append(".").append(f.getName());
                        temp = sb.toString().replace(".class", "");
//                        System.err.println(temp);
                        annotation = filterClassEntity(temp);
                        if (annotation != null) {
                            FILESENTITY.add(new LymytzEntity(temp, annotation));
                        }
                    }
                }

            }
        }
    }

    private String getSubPackage(File file) {
        String p = "";
        chemin.clear();
        rebuildChaineParent(file);
        if (!chemin.isEmpty()) {
            p = chemin.get(chemin.size() - 1);
            if (chemin.size() > 1) {
                for (int i = (chemin.size() - 2); i >= 0; i--) {
                    p = p + "." + chemin.get(i);
                }
            }
        }
        return p;
    }
    List<String> chemin;

    //reconstruire la chaine parent jusqu'à entity    
    private void rebuildChaineParent(File f) {
        if (f.getParentFile().getName().equals(path)) {
            chemin.add(path);
            return;
        } else {
            chemin.add(f.getParentFile().getName());
            rebuildChaineParent(f.getParentFile());
        }
    }

    private String filterClassEntity(String className) {
        if (className != null) {
            try {
                Class classe = Class.forName(className);
                Annotation[] annotations = classe.getAnnotations();
                if (annotations != null) {
                    for (Annotation a : annotations) {
                        if (a instanceof javax.persistence.Table) {
                            return ((Table) a).name();
                        }
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LymytzEntityLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(LymytzEntityLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private List<T> loadDataToSynchro() {
        List<T> re = null;
        Requete query = new Requete();
        List<YvsSynchroListenTable> listens = query.loadNameQueries("YvsSynchroListenTable.findByToListen", new String[]{"toListen"}, new Object[]{true}, 0, 100);

        return re;
    }

    public void builderEntity(List<YvsSynchroListenTable> allTables) {
        for (YvsSynchroListenTable t : allTables) {
//            if(t.get)
        }
    }

    /**
     * Cette classe permet de filtrer les fichiers d'un répertoire. Il n'accepte
     * que les fichiers .class.
     */
    private class DotClassFilter implements FilenameFilter {

        @Override
        public boolean accept(File arg0, String arg1) {
            return arg1.endsWith(".class");
        }

    }
}
