/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.init;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.connexion.Loggin;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsConnection;
import yvs.entity.users.YvsAutorisationModule;
import yvs.entity.users.YvsAutorisationPageModule;
import yvs.entity.users.YvsAutorisationRessourcesPage;
import yvs.entity.users.YvsModule;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsNiveauUsers;
import yvs.entity.users.YvsPageModule;
import yvs.entity.users.YvsRessourcesPage;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.parametrage.societe.Societe;
import yvs.parametrage.agence.Agence;
import yvs.base.produits.Articles;
import yvs.dao.Options;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.grh.presence.YvsPointeuse;
import yvs.entity.param.YvsModuleActive;
import yvs.entity.production.YvsProdParametre;
import yvs.entity.users.YvsUsersGrade;
import yvs.users.Users;
import yvs.users.messagerie.Conversation;
import yvs.util.Constantes;
import yvs.util.FileInfo;
import yvs.util.Managed;
import yvs.util.MdpUtil;
import yvs.util.Util;
//import yvs.util.Util;

/**
 *
 * @author Yves
 */
@ManagedBean(name = "init")
@SessionScoped
public class Initialisation extends Managed implements Serializable {
//    //information de connection

    public static DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");

    private Societe societe = new Societe();
    private Agence agence = new Agence();
    private Users users = new Users();

    private String logo;
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String USER_HOME = System.getProperty("user.home");

    public static final String USER_DOWNLOAD = System.getProperty("user.home") + System.getProperty("file.separator") + "Downloads";
    public static final String USER_DOWNLOAD_LINUX = System.getProperty("user.home") + System.getProperty("file.separator") + "yves" + System.getProperty("file.separator") + "downloads";
    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static boolean INITIALISATION = false;
    public static boolean USERISSUPERADMIN = false;
    public static boolean CLIENTWEB = false;
    public long MAX = Long.MAX_VALUE;
    public static String CHEMIN_PHOTOS_ART_ = "documents/docArticle/";

    //1iere configuration
    YvsSocietes entitySociete;
    YvsAgences entityAgence;
    YvsNiveauAcces entityNiveau;
    YvsUsers entityUser;
    YvsMutMutuelle entityMutuel;

    private List<YvsModuleActive> modules;

    private boolean skip;

    @ManagedProperty(value = "#{loggin}")
    private Loggin loggin;

    public Initialisation() {
        modules = new ArrayList<>();
    }

    public static boolean isUSERISSUPERADMIN() {
        return USERISSUPERADMIN;
    }

    public static void setUSERISSUPERADMIN(boolean USERISSUPERADMIN) {
        Initialisation.USERISSUPERADMIN = USERISSUPERADMIN;
    }

    public List<YvsModuleActive> getModules() {
        return modules;
    }

    public void setModules(List<YvsModuleActive> modules) {
        this.modules = modules;
    }

    public Loggin getLoggin() {
        return loggin;
    }

    public static boolean isCLIENTWEB() {
        return CLIENTWEB;
    }

    public static void setCLIENTWEB(boolean CLIENTWEB) {
        Initialisation.CLIENTWEB = CLIENTWEB;
    }

    public long getMAX() {
        return MAX;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public void setLoggin(Loggin loggin) {
        this.loggin = loggin;
    }

//    public void loadInfosSociete() {
//        entitySociete = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
//        societe = UtilSte.buildBeanSociete(entitySociete);
//        if (!entitySociete.getAgences().isEmpty()) {
//            entityAgence = entitySociete.getAgences().get(0);
//            agence = UtilAgence.buildBeanAgence(entityAgence);
//        }
//        if (entityAgence != null) {
//            entityUser = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByAgence", new String[]{"agence", "actif"}, new Object[]{entityAgence, true});
//            users = UtilUsers.buildBeanUsers(entityUser);
//        }
//    }
    int i = 0;

    public void preRenderView() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        } catch (Exception ex) {
            Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostConstruct
    public void loadConfig() {
        try {
            i++;
            if (!loggin.isConnecter()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("formatDate", "dd-MM-yyyy");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("formatDateTime", "dd-MM-yyyy HH:mm");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("giveRistourne", true);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("giveComission", true);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("longueurCompte", 9);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("devise", "FCFA");
                List<YvsSocietes> listS = dao.loadNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
                if (!listS.isEmpty()) {
                    entitySociete = listS.get(0);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("societ", entitySociete);
                    entityNiveau = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findBySuperAdmin", new String[]{"societe", "superAdmin"}, new Object[]{entitySociete, true});
                    if (entityNiveau == null) {
                        entityNiveau = buildYvsNiveauAcces(null);
                    }
                    List<YvsAgences> listA = dao.loadNameQueries("YvsAgences.findBySociete", new String[]{"societe"}, new Object[]{listS.get(0)});
                    if (!listA.isEmpty()) {
                        entityAgence = listA.get(0);
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agenc", entityAgence);
                        for (YvsAgences a : listA) {
                            List<YvsUsersAgence> listU = dao.loadNameQueries("YvsUsersAgence.findByAgence", new String[]{"agence"}, new Object[]{a});
                            if (!listU.isEmpty()) {
                                YvsUsersAgence author = listU.get(0);
                                entityUser = author.getUsers();
                                entityUser.setNiveauAcces(findNivo(entityUser, entitySociete));
                                entityUser.setAccesMultiAgence(true);
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", author);
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("codeUser", entityUser.getCodeUsers());
                                break;
                            }
                        }
                    }
                } else { //on suppose qu'aucune société enregistré signifie qu'on est à la première initialisation
//                onFirstInitialisation();
                }
                loggin.loadAcces(entityUser);
            } else {
                entitySociete = (YvsSocietes) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("societ");
                YvsComptaParametre pCompta = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{entitySociete});
                YvsComParametre pcom = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{entitySociete});
                YvsProdParametre pProd = (YvsProdParametre) dao.loadOneByNameQueries("YvsProdParametre.findAll", new String[]{"societe"}, new Object[]{entitySociete});
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramCompta", pCompta);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramCom", pcom);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramProd", pProd);
            }
            initializeAndSynchonizeFile();
        } catch (Exception ex) {
            Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

        } catch (Exception ex) {
            Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private YvsNiveauAcces findNivo(YvsUsers u, YvsSocietes scte) {
        for (YvsNiveauUsers nu : u.getNiveauxAcces()) {
            if (nu.getIdNiveau().getSociete().equals(scte)) {
                return nu.getIdNiveau();
            }
        }
        return null;
    }

    private void upgradeDataBase() {
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "upgrade");
            File folder = new File(path);
            if (folder.exists()) {
                File upgrade = new File(path + FILE_SEPARATOR + "upgrade.txt");
                Date date = new Date();
                if (upgrade.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(upgrade));
                    String st;
                    while ((st = br.readLine()) != null) {
                        date = formatDate.parse(st);
                    }
                }
                File[] listOfFiles = folder.listFiles();
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        FileInfo info = new FileInfo(file);
                        if (info.getExtension().toLowerCase().equals("sql")) {
                            if (!info.getLastModified().before(date)) {
                                Util.executeSqlFile(ds, info.getAbsolutePath());
                            }
                        }
                    } else if (file.isDirectory()) {
                    }
                }
                if (upgrade.exists()) {
                    upgrade.delete();
                }
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + FILE_SEPARATOR + "upgrade.txt"), "utf-8"))) {
                    writer.write(formatDate.format(new Date()));
                }
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeAndSynchonizeFile() {
        if (entitySociete != null) {
            initRepResource(entitySociete.getCodeAbreviation());
            if (entitySociete.getLogo() != null) {
                logo = Util.CheminLogo() + "" + entitySociete.getLogo();
            } else {
                logo = Util.CheminLogo() + "default.png";
            }
            logo = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "logo-moyen.png");
        }
        synchroniseFile();
    }

    public void initial() {
        getParametreMap();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
    private static String cheminLogo, cheminSave, cheminAllDoc, cheminDocEmps, CheminDocEnterprise, cheminDocUsers, cheminPhotoEmps, cheminDocPersoEmps,
            cheminDocArticle, cheminDocMut, cheminDocPointage, cheminDocLogs;

    public static void initRepResource(String nameScte) {
        StringBuilder home = new StringBuilder(USER_HOME);
        home.append(FILE_SEPARATOR).append("lymytz");
        File file = new File(home.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des logs
        cheminDocLogs = home.append(FILE_SEPARATOR).append("logs").toString();
        file = new File(cheminDocLogs);
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb = new StringBuilder(USER_HOME);
        StringBuilder sd = new StringBuilder(USER_HOME);
        sb.append(FILE_SEPARATOR).append("lymytz");
        sd.append(FILE_SEPARATOR).append("lymytz");
        if (nameScte != null) {
            sb.append(FILE_SEPARATOR).append(nameScte.replace(" ", "")).append(FILE_SEPARATOR);
            sd.append(FILE_SEPARATOR).append(nameScte.replace(" ", "")).append(FILE_SEPARATOR);
        }
        file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(sd.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des documents d'entreprise
        cheminAllDoc = sb.append("documents").toString();
        file = new File(cheminAllDoc);
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des documents d'entreprise
        cheminSave = sd.append("download").toString();
        file = new File(cheminSave);
        if (!file.exists()) {
            file.mkdirs();
        }

        StringBuilder sb0 = new StringBuilder(sb.toString());
        StringBuilder sb1 = new StringBuilder(sb.toString());
        StringBuilder sb2 = new StringBuilder(sb.toString());
        StringBuilder sb3 = new StringBuilder(sb.toString());
        StringBuilder sb4 = new StringBuilder(sb.toString());
        StringBuilder sb5 = new StringBuilder(sb.toString());
        StringBuilder sb6 = new StringBuilder(sb.toString());
        //chemin des documents mutuelle
        cheminDocMut = sb5.append(FILE_SEPARATOR).append("docMutuelle").toString();
        file = new File(cheminDocMut);
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des documents employes
        cheminDocEmps = sb1.append(FILE_SEPARATOR).append("docEmps").toString();
        file = new File(cheminDocEmps);
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des documents personnel employés
        cheminDocPersoEmps = sb1.append(FILE_SEPARATOR).append("perso").toString();
        file = new File(cheminDocPersoEmps);
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des photos employés
        cheminPhotoEmps = sb1.append(FILE_SEPARATOR).append("photo").toString();
        file = new File(cheminPhotoEmps);
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemins logo
        cheminLogo = sb0.append(FILE_SEPARATOR).append("logos_doc").toString();
        file = new File(cheminLogo);
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des documents de la société
        CheminDocEnterprise = sb2.append(FILE_SEPARATOR).append("docEnterprise").toString();
        file = new File(CheminDocEnterprise);
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des documents des utilisateurs
        cheminDocUsers = sb3.append(FILE_SEPARATOR).append("docUsers").toString();
        file = new File(cheminDocUsers);
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des documents des article
        cheminDocArticle = sb4.append(FILE_SEPARATOR).append("docArticle").toString();
        file = new File(cheminDocArticle);
        if (!file.exists()) {
            file.mkdirs();
        }
        //chemin des documents des pointages
        cheminDocPointage = sb6.append(FILE_SEPARATOR).append("pointage").toString();
        file = new File(cheminDocPointage);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getCheminArticles() {
        if (cheminDocArticle == null) {
            return "";
        }
        File file = new File(cheminDocArticle);
        if (!file.exists()) {
            file.mkdirs();
        }
        return cheminDocArticle;
    }

    public static String getCheminArticle(Articles article) {
        if (cheminDocArticle == null) {
            return "";
        }
        File file = new File(cheminDocArticle);
        if (!file.exists()) {
            file.mkdirs();
        }
//        String chemin = sb.toString();
//        String chemin = sb.append(FILE_SEPARATOR).append(article.getRefArt()).toString();
//        file = new File(chemin);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
        return cheminDocArticle;
    }

    public static String getCheminDocLogs() {
        if (cheminDocLogs == null) {
            return "";
        }
        File file = new File(cheminDocLogs);
        if (!file.exists()) {
            file.mkdirs();
        }
        return cheminDocLogs;
    }

    public static String getCheminPointage(YvsPointeuse pointeuse) {
        StringBuilder sb = new StringBuilder(cheminDocPointage);
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        String adresseIp = "127.0.0.1";
        if (pointeuse != null) {
            adresseIp = pointeuse.getAdresseIp();
        }
        String chemin = sb.append(FILE_SEPARATOR).append(adresseIp).toString();
        file = new File(chemin);
        if (!file.exists()) {
            file.mkdirs();
        }
        return sb.toString();
    }

    public static String getCheminMailUser(Conversation mail) {
        StringBuilder sb = new StringBuilder(cheminDocUsers);
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        String cheminUser = sb.append(FILE_SEPARATOR).append(mail.getEmetteur().getNomUsers().replace(" ", "_")).toString();
        file = new File(cheminUser);
        if (!file.exists()) {
            file.mkdirs();
        }
        String cheminMail = sb.append(FILE_SEPARATOR).append("Mail").toString();
        file = new File(cheminMail);
        if (!file.exists()) {
            file.mkdirs();
        }
        String chemin = sb.append(FILE_SEPARATOR).append(mail.getReference().replace(" ", "_")).toString();
        file = new File(chemin);
        if (!file.exists()) {
            file.mkdirs();
        }
        return chemin;
    }

    public static String getCheminDocMut() {
        return cheminDocMut;
    }

    public static String getCheminDocPointage() {
        return cheminDocPointage;
    }

    public static String getCheminArticle() {
        return cheminDocArticle;
    }

    public static String getCheminSave() {
        return cheminSave;
    }

    public static String getCheminLogo() {
        return cheminLogo;
    }

    public static void setCheminLogo(String cheminLogo) {
        Initialisation.cheminLogo = cheminLogo;
    }

    public static String getCheminLogos() {
        return cheminLogo;
    }

    public static String getCheminDocEmps() {
        return cheminDocEmps;
    }

    public static String getCheminDocEnterprise() {
        return CheminDocEnterprise;
    }

    public static String getCheminDocUsers() {
        return cheminDocUsers;
    }

    public static String getCheminPhotoEmps() {
        return cheminPhotoEmps;
    }

    public static String getCheminDocPersoEmps() {
        return cheminDocPersoEmps;
    }

    public static String getCheminAllDoc() {
        return cheminAllDoc;
    }

    public static String getCheminResource() {
        return new StringBuilder(FILE_SEPARATOR).append("resources").append(FILE_SEPARATOR).append("lymytz").append(FILE_SEPARATOR).append("documents").toString();
    }

    public static String getCheminDownload() {
        return new StringBuilder(FILE_SEPARATOR).append("resources").append(FILE_SEPARATOR).append("lymytz").append(FILE_SEPARATOR).append("download").toString();
    }

    public static String getCheminLogs() {
        return new StringBuilder(FILE_SEPARATOR).append("resources").append(FILE_SEPARATOR).append("lymytz").append(FILE_SEPARATOR).append("logs").toString();
    }

    private void synchroniseFile() {
        if (cheminAllDoc != null) {
            synchroniseFile(new File(cheminAllDoc));
        }
    }

    private void synchroniseFile(File files) {
        String repDest = getCheminResource();
        File fichier = new File(repDest);
        //création du répertoire racine des documents s'il n'existe pas
        if (!fichier.exists()) {
            fichier.mkdirs();
        }
        if ((files.exists()) ? files.listFiles().length != 0 : false) {
            try {
                for (File file : files.listFiles()) {
                    String chemin = file.getAbsolutePath();
                    chemin = chemin.substring(cheminAllDoc.length(), chemin.length());
                    repDest += FILE_SEPARATOR + chemin;
                    if (file.isDirectory()) {
                        Util.copyDirectory(repDest);
                    } else if (file.isFile()) {
                        Util.copyFile("", repDest, new FileInputStream(file));
                    }
                    repDest = getCheminResource();
                    if (file.listFiles() != null) {
                        synchroniseFile(file);
//                        repDest = "/resources/lymytz/documents/";

                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Initialisation.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void synchroniseFile_OLD(File files) {
        String repDest = getCheminResource();
        File fichier = new File(repDest);
        //création du répertoire racine des documents s'il n'existe pas
        if (!fichier.exists()) {
            fichier.mkdirs();
        }
        if ((files.exists()) ? files.listFiles().length != 0 : false) {
            try {
                for (File file : files.listFiles()) {
                    String chemin = file.getAbsolutePath();
                    chemin = chemin.substring(cheminAllDoc.length(), chemin.length());
                    if (file.isDirectory()) {
                        repDest += FILE_SEPARATOR + chemin;
                        Util.copyDirectory(repDest);
                        repDest = getCheminResource();
                    } else if (file.isFile()) {
                        repDest += "" + chemin;
                        Util.copyFile("", repDest, new FileInputStream(file));
                        repDest = getCheminResource();
                    }
                    if (file.listFiles() != null) {
                        synchroniseFile(file);
//                        repDest = "/resources/lymytz/documents/";

                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Initialisation.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private MdpUtil hashMdp = new MdpUtil();

    public MdpUtil getHashMdp() {
        return hashMdp;
    }

    public void setHashMdp(MdpUtil hashMdp) {
        this.hashMdp = hashMdp;
    }

    private YvsAgences buildAgence(Agence bean) {
        YvsAgences result = new YvsAgences();
        if (bean != null) {
            result.setId(bean.getId());
            result.setAbbreviation(bean.getAbbreviation());
            result.setSupp(false);
            result.setActif(true);
            result.setAdresse(bean.getAdresse());
            result.setDesignation(bean.getDesignation());
            result.setRegion(bean.getRegion());
            result.setTelephone(bean.getTelephone());
            result.setCodeagence(bean.getCodeAgence());
            result.setEmail(bean.getEmail());
            result.setSociete(entitySociete);
        }
        return result;
    }

    public YvsSocietes buildSociete(Societe ste) {
        YvsSocietes society = new YvsSocietes();
        if (ste != null) {
            society.setId(ste.getId());
            society.setName(ste.getRaisonSocial());
            society.setAdressSiege(ste.getAdressSiege());
            society.setCapital(ste.getCapital());
            society.setCodeAbreviation(ste.getCodeAbreviation());
            society.setCodePostal(ste.getCodePostal());
            society.setDevise(ste.getDevise());
            society.setEmail(ste.getEmail());
            society.setFormeJuridique(ste.getFormeJuridique());
            society.setNumeroRegistreComerce(ste.getNumeroRegistreCom());
            society.setSiege(ste.getSiege());
            society.setSiteWeb(ste.getSiteWeb());
            society.setTel(ste.getTelephone());
            society.setActif(true);
            society.setSupp(false);
        }
        return society;
    }

    public YvsUsers buildUsers(Users e) {
        YvsUsers b = new YvsUsers();
        if (e != null) {
            b.setId(e.getId());
            b.setAccesMultiAgence(true);
            b.setAccesMultiSociete(true);
            b.setDateSave(new Date());
            b.setDateUpdate(new Date());
            b.setCivilite(Constantes.CIVILITE_M);
            b.setActif(true);
            b.setNomUsers(e.getNomUsers());
            b.setAleaMdp(hashMdp.randomString(15));
            b.setPasswordUser(hashMdp.hashString(e.getCodeUsers() + "" + b.getAleaMdp() + "" + e.getPasswordUser()));
            b.setCodeUsers(e.getCodeUsers());
            b.setConnecte(false);
            b.setSuperAdmin(true);
            b.setAgence(entityAgence);
            b.setSupp(false);
        }
        return b;
    }

    public YvsAutorisationModule buildYvsAutorisationModule(YvsNiveauAcces niv, YvsModule mod) {
        if (niv != null && mod != null) {
            YvsAutorisationModule a = (YvsAutorisationModule) dao.loadOneByNameQueries("YvsAutorisationModule.findByModuleNiveau", new String[]{"niveau", "module"}, new Object[]{niv, mod});
            if (a == null) {
                a = new YvsAutorisationModule();
                a.setAcces(true);
                a.setModule(mod);
                a.setNiveauAcces(niv);
            }
            return a;
        }
        return null;
    }

    public YvsAutorisationPageModule buildYvsAutorisationPageModule(YvsNiveauAcces niv, YvsPageModule pag) {
        YvsAutorisationPageModule a = new YvsAutorisationPageModule();
        if (niv != null && pag != null) {
            a.setAcces(true);
            a.setPageModule(pag);
            a.setNiveauAcces(niv);
        }
        return a;
    }

    public YvsAutorisationRessourcesPage buildYvsAutorisationRessourcesPage(YvsNiveauAcces niv, YvsRessourcesPage ress) {
        YvsAutorisationRessourcesPage a = new YvsAutorisationRessourcesPage();
        if (niv != null && ress != null) {
            a.setAcces(true);
            a.setRessourcePage(ress);
            a.setNiveauAcces(niv);
        }
        return a;
    }

    public YvsNiveauAcces buildYvsNiveauAcces(YvsUsersGrade grade) {
        YvsNiveauAcces n = null;
        if (societe != null ? societe.getId() > 0 : false) {
            n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findSuperAdmin", new String[]{}, new Object[]{});
        }
        if (n == null) {
            n = new YvsNiveauAcces();
            n.setActif(true);
            if (users.getNiveauAcces() != null) {
                n.setDescription((users.getNiveauAcces().getDescription().isEmpty()) ? "Administrateur de l'application" : users.getNiveauAcces().getDescription());
                n.setDesignation((users.getNiveauAcces().getDesignation().isEmpty()) ? "SUPER ADMINISTRATEUR" : users.getNiveauAcces().getDesignation());
            } else {
                n.setDesignation("SUPER ADMINISTRATEUR");
            }
            n.setGrade(grade);
            n.setSociete(entitySociete);
            n.setSuperAdmin(true);
            n.setSupp(false);
            n.setDateSave(new Date());
            n.setDateUpdate(new Date());
            n = (YvsNiveauAcces) dao.save1(n);
        }
        if (n.getId() > 0) {
            List<Integer> ids = new ArrayList<>();
            for (YvsModuleActive m : modules) {
                if (m.getActif()) {
                    if (!ids.contains(m.getModule().getId())) {
                        ids.add(m.getModule().getId());
                    }
                    YvsAutorisationModule aum = buildYvsAutorisationModule(n, m.getModule());
                    if (aum != null) {
                        dao.update(aum);
                    }
                }
            }
            List<YvsPageModule> listP = dao.loadNameQueries("YvsPageModule.findByModules", new String[]{"modules"}, new Object[]{ids});
            for (YvsPageModule p : listP) {
                YvsAutorisationPageModule aum = buildYvsAutorisationPageModule(n, p);
                if (aum != null) {
                    dao.update(aum);
                }
            }
            List<YvsRessourcesPage> listR = dao.loadNameQueries("YvsRessourcesPage.findByModules", new String[]{"modules"}, new Object[]{ids});
            for (YvsRessourcesPage r : listR) {
                YvsAutorisationRessourcesPage aum = buildYvsAutorisationRessourcesPage(n, r);
                if (aum != null) {
                    dao.update(aum);
                }
            }
        }
        return n;
    }

    public void saveAll() {
        try {
            entitySociete = buildSociete(societe);
            if (societe.getId() <= 0) {
                entitySociete.setId(null);
                entitySociete.setDateSave(new Date());
                entitySociete.setLastDateSave(new Date());
                entitySociete = (YvsSocietes) dao.save1(entitySociete);
            } else {
                dao.update(entitySociete);
            }
            entityAgence = buildAgence(agence);
            if (agence.getId() <= 0) {
                entityAgence.setId(null);
                entityAgence.setDateSave(new Date());
                entityAgence.setLastDateSave(new Date());
                entityAgence = (YvsAgences) dao.save1(entityAgence);
            } else {
                dao.update(entityAgence);
            }
            //Création du premier grade
            YvsUsersGrade ug = new YvsUsersGrade();
            ug.setDateSave(new Date());
            ug.setDateUpdate(new Date());
            ug.setLibelle("ADMINISTRATEURS");
            ug.setReference("A");
            ug = (YvsUsersGrade) dao.save1(ug);
            entityNiveau = buildYvsNiveauAcces(ug);
            YvsUsers user = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByCodeUsers", new String[]{"codeUsers"}, new Object[]{users.getCodeUsers()});
            if (user == null) {
                user = buildUsers(users);
                user = (YvsUsers) dao.save1(user);
            } else {
                user = buildUsers(users);
                user.setId(null);
                dao.update(user);
            }
            //Save user agence et modifie les relation
            YvsUsersAgence ua = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findByUsersAgence", new String[]{"user", "agence"}, new Object[]{user, entityAgence});
            if (ua == null) {
                ua = new YvsUsersAgence();
                ua.setAgence(entityAgence);
                ua.setConnecte(true);
                ua.setDateSave(new Date());
                ua.setDateUpdate(new Date());
                ua.setLastConnexion(new Date());
                ua.setCanAction(true);
                ua.setActif(true);
                ua.setUsers(user);
                ua = (YvsUsersAgence) dao.save1(ua);
            }
            if (ua != null) {
                entitySociete.setAuthor(ua);
                dao.update(entitySociete);
                entityAgence.setAuthor(ua);
                dao.update(entityAgence);
                entityNiveau.setAuthor(ua);
                dao.update(entityNiveau);
                user.setAuthor(ua);
                dao.update(user);
            }
            if (entityNiveau != null && user != null) {
                YvsNiveauUsers nu = (YvsNiveauUsers) dao.loadOneByNameQueries("YvsNiveauUsers.findNivoUser", new String[]{"niveau", "user"}, new Object[]{entityNiveau, user});
                if (nu == null) {
                    nu = new YvsNiveauUsers();
                    nu.setDateSave(new Date());
                    nu.setDateUpdate(new Date());
                    nu.setIdNiveau(entityNiveau);
                    nu.setIdUser(user);
                    dao.save(nu);
                }
            }
            for (YvsModuleActive m : modules) {
                m.setSociete(entitySociete);
                if (ua != null) {
                    m.setAuthor(ua);
                }
                m.setId(null);
                dao.save(m);
            }
            USERISSUPERADMIN = true;

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agenc", entityAgence);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", ua);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + Constantes.ADRESSE + "/lymytz/pages/yvs_accueil.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Initialisation impossible", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String onFlowProcess(FlowEvent event) {
//            String id = event.getNewStep().
        if (skip) {
            skip = false;   //reset in case users goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    public static void onFirstInitialisation() {
        try {
            INITIALISATION = true;
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + Constantes.ADRESSE + "/Initialisation");
        } catch (IOException ex) {
            Logger.getLogger(Initialisation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        INITIALISATION = true;
    }

    public void getParametreMap() {
        String module = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("module");
        if ((module != null) ? !module.equals("") : false) {
            CLIENTWEB = true;
            long id = Long.valueOf(module);
            getInfosBase(id);
        } else {
            CLIENTWEB = false;
        }
    }

    private void getInfosBase(long id) {
        YvsConnection c = (YvsConnection) dao.loadOneByNameQueries("YvsConnection.findById", new String[]{"id"}, new Object[]{id});
        if ((c != null) ? c.getId() > 0 : false) {
            entityUser = c.getUsers();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", c.getUsers());
            entityAgence = c.getAgence();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agenc", c.getAgence());
            if ((entityAgence != null) ? entityAgence.getId() > 0 : false) {
                entitySociete = entityAgence.getSociete();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("societ", entityAgence.getSociete());
                if ((entityAgence.getMutuelle() != null) ? entityAgence.getMutuelle().getId() > 0 : false) {
                    entityMutuel = entityAgence.getMutuelle();
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mutuel", entityMutuel);
                }
            }
        } else {
            CLIENTWEB = false;
        }
    }

    public void buildModuleActive() {
        modules.clear();
        String query = "SELECT m.id, m.libelle, m.description FROM yvs_module m";
        List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{});
        if (result != null ? !result.isEmpty() : false) {
            YvsModule m;
            YvsModuleActive a;
            for (Object[] data : result) {
                m = new YvsModule((Integer) data[0]);
                m.setLibelle((String) data[1]);
                m.setDescription((String) data[2]);
                a = new YvsModuleActive(YvsModuleActive.ids--);
                a.setActif(false);
                a.setAuthor(currentUser);
                a.setModule(m);
                modules.add(a);
            }
        }
    }

    public void activeModule(YvsModuleActive m) {
        if (m != null) {
            int index = modules.indexOf(m);
            if (index > -1) {
                modules.set(index, m);
            }
        }
    }

    public void createModuleApplication() {
        Long nb = (Long) dao.loadObjectByNameQueries("YvsModule.countModule", new String[]{}, new Object[]{});
        if ((nb != null ? nb > 0 : false)) {
            getInfoMessage("Des modules ont été trouvé !");
            return;
        }
        YvsModule mod = new YvsModule(null, "param_", "Paramétrage", "Module des données de bases applications");
        mod.setDateSave(new Date());
        mod.setDateUpdate(new Date());
        dao.save(mod);
        mod = new YvsModule(null, "grh_", "Ressources Humaines", "Module de gestion des RH");
        mod.setDateSave(new Date());
        mod.setDateUpdate(new Date());
        dao.save(mod);
        mod = new YvsModule(null, "com_", "Gestion commerciale", "Module de Gestion commerciale");
        mod.setDateSave(new Date());
        mod.setDateUpdate(new Date());
        dao.save(mod);
        mod = new YvsModule(null, "prod_", "Production", "Module de Gestion commerciale");
        mod.setDateSave(new Date());
        mod.setDateUpdate(new Date());
        dao.save(mod);
        mod = new YvsModule(null, "compte_", "Comptabilité et Finance", "Module de Gestion Comptabilité et Finance");
        mod.setDateSave(new Date());
        mod.setDateUpdate(new Date());
        dao.save(mod);
        mod = new YvsModule(null, "client_", "RelationsClients", "Module de Gestion RC");
        mod.setDateSave(new Date());
        mod.setDateUpdate(new Date());
        dao.save(mod);
        mod = new YvsModule(null, "mutuel_", "Mutuelle", "Module de Gestion des mutuelles");
        mod.setDateSave(new Date());
        mod.setDateUpdate(new Date());
        dao.save(mod);
        mod = new YvsModule(null, "base_", "Données de bases", "Module de Gestion des Données de bases");
        mod.setDateSave(new Date());
        mod.setDateUpdate(new Date());
        dao.save(mod);
        mod = new YvsModule(null, "stat_", "Statistiques", "Statistiques");
        mod.setDateSave(new Date());
        mod.setDateUpdate(new Date());
        dao.save(mod);
        succes();

    }

    @Override
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
