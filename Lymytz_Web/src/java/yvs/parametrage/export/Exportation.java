/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.export;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.param.YvsDictionnaire;
import yvs.util.Utilitaire;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "export")
public class Exportation implements Serializable {

    private DualListModel<String> listDico;
    private List<String> sourceDico;
    private List<String> targetDico;
    ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
    @EJB
    public DaoInterfaceLocal dao;

    public Exportation() {
        sourceDico = new ArrayList<>();
        targetDico = new ArrayList<>();
        loadChampDico();
        listDico = new DualListModel<>(sourceDico, targetDico);
    }

    public DualListModel<String> getListDico() {
        return listDico;
    }

    public void setListDico(DualListModel<String> listDico) {
        this.listDico = listDico;
    }

    public List<String> getSourceDico() {
        return sourceDico;
    }

    public void setSourceDico(List<String> sourceDico) {
        this.sourceDico = sourceDico;
    }

    public List<String> getTargetDico() {
        return targetDico;
    }

    public void setTargetDico(List<String> targetDico) {
        this.targetDico = targetDico;
    }

    private void loadChampDico() {
        try {
            Field[] champs = Class.forName("yvs.entity.param.YvsDictionnaire").getDeclaredFields();
            sourceDico.clear();
            for (Field f : champs) {
                if (f.isAnnotationPresent(Column.class)) {
//                    Column c = f.getAnnotation(Column.class);
                    sourceDico.add(f.getName());
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void callExport() {
        System.out.println("Target " + targetDico.size());
        dao.setEntityClass(YvsDictionnaire.class);
        List<YvsDictionnaire> l = dao.loadAllEntity();
        exporter(l);
    }

    private void exporter(List<YvsDictionnaire> l) {
        File file = new File(ext.getRealPath("art.txt"));
        try {
            bfw = new BufferedWriter(new FileWriter(file));
            for (YvsDictionnaire dico : l) {
                int i = 0;
                StringBuilder sb = new StringBuilder();
                while (i < listDico.getTarget().size()) {
                    try {
                        Class c = dico.getClass();
                        Field f = accessible(c, listDico.getTarget().get(i));
                        //cas des dates
                        Object ob = null;
                        if (f.getType().equals(Date.class)) {
                            DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                            ob = df.format(f.get(dico));
                        } else {
                            ob = f.get(dico);
                        }
                        sb.append(ob);
                        sb.append("\t");
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    i++;
                }
                //je sort d'ici avec un String builder
                bfw.write(sb.toString());
                bfw.newLine();

            }
        } catch (IOException ex) {
            Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bfw.close();
            } catch (IOException ex) {
                Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Utilitaire.ouvrirLeFlux(Utilitaire.readFile(file), "article.txt", "txt");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Teminé", " "));
    }
    BufferedWriter bfw = null;

    public void transfert(TransferEvent ev) {
        for (Object s : ev.getItems()) {
//            System.out.println(s);
        }
    }
//methode pour Capitaliser une chaine de caractère

    public String capitalize(String st) {
        StringBuilder sb = null;
        if (st != null) {
            if (st.length() != 0) {
                String t = st.substring(0, 1).toUpperCase();
                sb = new StringBuilder(t);
                sb.append(st.substring(1, st.length()));
            }
        }
        return sb.toString();
    }

    public void insertInit() {
        String titre = "Pays";
        for (int i = 0; i < 10000; i++) {
            if (i == 2000) {
                titre = "Villes";
            }
            if (i == 3000) {
                titre = "Devises";
            }
            if (i == 5000) {
                titre = "Fonctions";
            }
            if (i == 6000) {
                titre = "Localisations";
            }
            YvsDictionnaire d = new YvsDictionnaire(titre, titre + " EN " + i);           
            dao.save(d);
            d = null;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Teminé", " "));
    }
    /**
     * Importer les données**
     */
    List<YvsDictionnaire> lisResult = new ArrayList<>();

    public void importer() {
        File file = new File(ext.getRealPath("art.txt"));
        importer(file);
    }

    public void importer(File f) {
        try {
            String ligne;
            String[] tabLine;
            BufferedReader fichier = new BufferedReader(new FileReader(f));
            YvsDictionnaire d = null;
            while ((ligne = fichier.readLine()) != null) {
                int i = 0;
                d = new YvsDictionnaire();
                tabLine = ligne.split("\t");
                Class c = d.getClass();
                if (tabLine.length != listDico.getTarget().size()) {
                    //erreur. le nombre de champ du fichier doit correspondre au nombre de champ choisit
                    System.err.println("erreur du nbre de champ");
                } else {
                    while (i < tabLine.length) {
                        d = convert(d, tabLine[i], accessible(c, listDico.getTarget().get(i)));
                        i++;
                    }
                }
                //ici, on a fabriqué l'objet dico
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            }            
        } catch (IOException ex) {
            Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //rend accessible un champ privé d'une classe
    private Field accessible(Class c, String name) {
        for (Field f : c.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }
//chacun des cas de conversion doit pouvoir lever une exception en cas d'impossibilité de conversion.
    private YvsDictionnaire convert(YvsDictionnaire dico, Object val, Field field) {
        try {
            System.out.println(field.getType().getSimpleName());
            switch (field.getType().getSimpleName()) {
                case "Date":
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    Date d = null;
                    try {
                        d = df.parse((String) val);
                    } catch (ParseException ex) {
                        Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    field.set(dico, d);
                    break;
                case "double":
                    field.set(dico, Double.valueOf((String) val));
                    break;
                case "boolean":
                    field.set(dico, Boolean.valueOf((String) val));
                    break;
                case "long":
                    field.set(dico, Long.valueOf((String) val));
                    break;
                default:
                    field.set(dico, (String) val);
                    break;
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Exportation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dico;
    }
}
