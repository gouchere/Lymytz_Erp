/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws;

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import javax.ejb.EJB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang3.SerializationUtils;
import yvs.dao.DaoInterfaceWs;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowAutorisationValidDoc;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
public abstract class ManagedWS implements Serializable {

    @EJB
    public DaoInterfaceWs dao;
    String[] champ;
    Object[] val;

    public static DateFormat dm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static DateFormat hf = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    public static DateFormat dj = new SimpleDateFormat("dddd", Locale.FRENCH);
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String USER_HOME = System.getProperty("user.home");

    YvsUsersAgence currentUser;
    YvsAgences currentAgence;
    YvsSocietes currentScte;
    YvsUsers currentUsers;
    YvsGrhEmployes currentEmploye;
    YvsParametreGrh currentParam;

    public YvsUsers onlineUsers;
    public YvsGrhTrancheHoraire onlineTranche;
    public YvsBaseCategorieComptable onlineCategorie;
    public YvsComClient defaultClient;
    public YvsSocietes onlineSociete;

    public static String PATH_PHOTO;

    public static String[] CHAMP_ARTICLE = new String[]{};
    public static Object[] VAL_ARTICLE = new Object[]{};
    public static String QUERY_ARTICLE;
    public static Long COUNT_ARTICLE;

    public abstract String getPathArticle();

    public String getPathIdentity(String nameScte) {
        StringBuilder sb = new StringBuilder(USER_HOME);
        sb.append(FILE_SEPARATOR).append("lymytz");
        if (nameScte != null) {
            sb.append(FILE_SEPARATOR).append(nameScte.replace(" ", "")).append(FILE_SEPARATOR);
        }
        File file = new File(sb.toString());
        if (!file.exists()) {
            return null;
        }
        //chemin des documents d'entreprise
        String documents = sb.append("documents").toString();
        file = new File(documents);
        if (!file.exists()) {
            return null;
        }
        StringBuilder sb1 = new StringBuilder(sb.toString());
        //chemin des documents employes
        String employe = sb1.append(FILE_SEPARATOR).append("docEmps").toString();
        file = new File(employe);
        if (!file.exists()) {
            return null;
        }
        //chemin des documents personnel employés
        String perso = sb1.append(FILE_SEPARATOR).append("perso").toString();
        file = new File(perso);
        if (!file.exists()) {
            return null;
        }
        //chemin des photos employés
        String photo = sb1.append(FILE_SEPARATOR).append("photo").toString();
        file = new File(photo);
        if (!file.exists()) {
            return null;
        }
        return photo;
    }

    public String getPathArticle(String nameScte) {
        StringBuilder sb = new StringBuilder(USER_HOME);
        sb.append(FILE_SEPARATOR).append("lymytz");
        if (nameScte != null) {
            sb.append(FILE_SEPARATOR).append(nameScte.replace(" ", "")).append(FILE_SEPARATOR);
        }
        File file = new File(sb.toString());
        if (!file.exists()) {
            return null;
        }
        //chemin des documents d'entreprise
        String documents = sb.append("documents").toString();
        file = new File(documents);
        if (!file.exists()) {
            return null;
        }

        StringBuilder sb4 = new StringBuilder(sb.toString());
        //chemin des documents des article
        String article = sb4.append(FILE_SEPARATOR).append("docArticle").toString();
        file = new File(article);
        if (!file.exists()) {
            return null;
        }
        return article;
    }

    public void load(long users) {
        currentUser = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{users});
        if (currentUser.getUsers() != null) {
            currentUser.getUsers().setNiveauAcces(findNivo(currentUser.getUsers(), currentUser.getUsers().getAgence().getSociete()));
            currentUsers = currentUser.getUsers();
        }
        if (currentUsers.getEmploye() != null) {
            currentEmploye = currentUsers.getEmploye();
        }
        if (currentUser.getAgence() != null) {
            currentAgence = currentUser.getAgence();
        }
        if (currentAgence.getSociete() != null) {
            currentScte = currentAgence.getSociete();
        }
        if (currentScte != null ? (currentScte.getYvsParametreGrh() != null ? !currentScte.getYvsParametreGrh().isEmpty() : false) : false) {
            currentParam = currentScte.getYvsParametreGrh().get(0);
        }
    }

    public String load(long users, long employe, long societe) {
        try {
            currentUsers = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findById", new String[]{"id"}, new Object[]{users});
            if (employe > 0) {
                currentEmploye = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{employe});
            } else {
                currentEmploye = currentUsers.getEmploye();
            }
            currentAgence = currentEmploye.getAgence();
            if (societe > 0) {
                currentScte = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{societe});
            } else {
                currentScte = currentAgence.getSociete();
            }
            if (currentScte != null ? (currentScte.getYvsParametreGrh() != null ? !currentScte.getYvsParametreGrh().isEmpty() : false) : false) {
                currentParam = currentScte.getYvsParametreGrh().get(0);
            }
            currentUsers.setNiveauAcces(findNivo(currentUsers, currentUsers.getAgence().getSociete()));
            return "OK";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public void loadClient(long adresse) {
        defaultClient = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findDefautVille", new String[]{"societe", "ville"}, new Object[]{getOnlineSocietes(), new YvsDictionnaire(adresse)});
        if (defaultClient != null ? defaultClient.getId() < 1 : true) {
            defaultClient = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findDefautPays", new String[]{"societe", "pays"}, new Object[]{getOnlineSocietes(), new YvsDictionnaire(adresse)});
        }
    }

    public void loadTranche() {
        onlineTranche = (YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findByOnline", new String[]{"societe", "venteOnline"}, new Object[]{getOnlineSocietes(), true});
    }

    public void loadUsers() {
        onlineUsers = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByOnline", new String[]{"societe", "venteOnline"}, new Object[]{getOnlineSocietes(), true});
    }

    public void loadCategorieComptable() {
        onlineCategorie = (YvsBaseCategorieComptable) dao.loadOneByNameQueries("YvsBaseCategorieComptable.findByOnline", new String[]{"societe", "venteOnline"}, new Object[]{getOnlineSocietes(), true});
    }

    public void loadSociete() {
        onlineSociete = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findByOnline", new String[]{"venteOnline"}, new Object[]{true});
    }

    public YvsSocietes getOnlineSocietes() {
        if (onlineSociete != null ? onlineSociete.getId() < 1 : true) {
            loadSociete();
        }
        return onlineSociete;
    }

    public YvsBaseCategorieComptable getOnlineCategorieComptable() {
        if (onlineCategorie != null ? onlineCategorie.getId() < 1 : true) {
            loadCategorieComptable();
        }
        return onlineCategorie;
    }

    public boolean asDroitValideEtape(YvsWorkflowEtapeValidation etape, YvsUsers users) {
        for (YvsWorkflowAutorisationValidDoc au : etape.getAutorisations()) {
            if (au.getNiveauAcces().equals(users.getNiveauAcces())) {
                return au.getCanValide();
            }
        }
        return false;
    }

    public YvsNiveauAcces findNivo(YvsUsers u, YvsSocietes scte) {
        for (YvsNiveauUsers nu : u.getNiveauxAcces()) {
            if (nu.getIdNiveau().getSociete().equals(scte)) {
                return nu.getIdNiveau();
            }
        }
        return null;
    }

    public double arrondi(double d) {
        return dao.arrondi(currentScte != null ? currentScte.getId() : 2297, d);
    }

    public static boolean asString(String valeur) {
        if (valeur != null ? valeur.trim().length() > 0 : false) {
            return true;
        }
        return false;
    }

    public Date timeToDate(Date time) {
        Calendar c = Calendar.getInstance();
        Calendar t = Calendar.getInstance();
        t.setTime(time);
        c.set(Calendar.HOUR, t.get(Calendar.HOUR));
        c.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
        c.set(Calendar.SECOND, t.get(Calendar.SECOND));
        return c.getTime();
    }

    public Date strToDate(String valeur) {
        Date date = new Date();
        if (valeur != null ? valeur.trim().length() > 0 : false) {
            valeur = valeur.trim();
            try {
                date = dm.parse(valeur);
            } catch (ParseException ex1) {
                try {
                    date = df.parse(valeur);
                } catch (ParseException ex2) {
                    try {
                        date = new Date(Date.parse(valeur));
                    } catch (Exception ex3) {
                        date = new Date();
                        System.err.println("date :  " + valeur);
                        ex3.printStackTrace();
                    }
                }
            }
        }
        return date;
    }

    public Date strToheure(String valeur) {
        Date date = new Date();
        if (valeur != null ? valeur.trim().length() > 0 : false) {
            valeur = valeur.trim();
            try {
                date = hf.parse(valeur);
            } catch (ParseException ex1) {
                try {
                    date = dm.parse(valeur);
                } catch (ParseException ex2) {
                    try {
                        date = new Date(Date.parse(valeur));
                    } catch (Exception ex3) {
                        date = new Date();
                        System.err.println("heure :  " + valeur);
                        ex3.printStackTrace();
                    }
                }
            }
        }
        return date;
    }

    public static List<Integer> convertArrayStringToInt(String[] entree) {
        List<Integer> result = new ArrayList<>();
        if (entree != null) {
            for (String e : entree) {
                result.add(Integer.valueOf(e != null ? e.trim().length() > 0 ? e.trim() : "0" : "0"));
            }
        }
        return result;
    }

    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        try {
            bytes = SerializationUtils.serialize((Serializable) obj);
        } catch (Exception ex) {
            Logger.getLogger(ManagedWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bytes;
    }

    public Object deserialize(byte[] bytes) {
        Object obj = null;
        try {
            obj = SerializationUtils.deserialize(bytes);
        } catch (Exception ex) {
            Logger.getLogger(ManagedWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    public byte[] encode(Object obj) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream out = new GZIPOutputStream(baos);
            try (XMLEncoder encoder = new XMLEncoder(out)) {
                encoder.writeObject(obj);
            }
            bytes = baos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    public Object decode(byte[] bytes) {
        Object obj = null;
        try {
            InputStream fis = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(fis);
            obj = o.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ManagedWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    public String generateXml(Object obj, Class objClass) {
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(objClass);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(obj, sw);
        } catch (JAXBException ex) {
            Logger.getLogger(ManagedWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sw.toString();
    }
}
