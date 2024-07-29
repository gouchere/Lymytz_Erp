/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.init;

import java.io.File;
import java.io.Serializable;
import yvs.entity.communication.YvsMsgConversation;
import yvs.entity.produits.YvsBaseArticles;
//import yvs.util.Util;

/**
 *
 * @author Yves
 */
public class Chemins implements Serializable {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String USER_HOME = System.getProperty("user.home");
    public static final String USER_DOWNLOAD = System.getProperty("user.home") + System.getProperty("file.separator") + "Downloads";

    private void initRepResource(String nameScte) {
        StringBuilder sb = new StringBuilder(USER_HOME);
        sb.append(FILE_SEPARATOR).append("lymytz");
        if (nameScte != null) {
            sb.append(FILE_SEPARATOR).append(nameScte.replace(" ", "")).append(FILE_SEPARATOR);
        }
        File file = new File(sb.toString());
        if (!file.exists()) {

        }
        //chemin des documents d'entreprise
        cheminAllDoc = sb.append("documents").toString();
        file = new File(cheminAllDoc);
        if (!file.exists()) {

        }

        StringBuilder sb0 = new StringBuilder(sb.toString());
        StringBuilder sb1 = new StringBuilder(sb.toString());
        StringBuilder sb2 = new StringBuilder(sb.toString());
        StringBuilder sb3 = new StringBuilder(sb.toString());
        StringBuilder sb4 = new StringBuilder(sb.toString());
        StringBuilder sb5 = new StringBuilder(sb.toString());
        //chemin des documents mutuelle
        cheminDocMut = sb5.append(FILE_SEPARATOR).append("docMutuelle").toString();
        file = new File(cheminDocMut);
        if (!file.exists()) {

        }
        //chemin des documents employes
        cheminDocEmps = sb1.append(FILE_SEPARATOR).append("docEmps").toString();
        file = new File(cheminDocEmps);
        if (!file.exists()) {

        }
        //chemin des documents personnel employés
        cheminDocPersoEmps = sb1.append(FILE_SEPARATOR).append("perso").toString();
        file = new File(cheminDocPersoEmps);
        if (!file.exists()) {

        }
        //chemin des photos employés
        cheminPhotoEmps = sb1.append(FILE_SEPARATOR).append("photo").toString();
        file = new File(cheminPhotoEmps);
        if (!file.exists()) {

        }
        //chemins logo
        cheminLogo = sb0.append(FILE_SEPARATOR).append("logos_doc").toString();
        file = new File(cheminLogo);
        if (!file.exists()) {

        }
        //chemin des documents de la société
        CheminDocEnterprise = sb2.append(FILE_SEPARATOR).append("docEnterprise").toString();
        file = new File(CheminDocEnterprise);
        if (!file.exists()) {

        }
        //chemin des documents des utilisateurs
        cheminDocUsers = sb3.append(FILE_SEPARATOR).append("docUsers").toString();
        file = new File(cheminDocUsers);
        if (!file.exists()) {

        }
        //chemin des documents des article
        cheminDocArticle = sb4.append(FILE_SEPARATOR).append("docArticle").toString();
        file = new File(cheminDocArticle);
        if (!file.exists()) {

        }
    }
    private static String cheminLogo, cheminAllDoc, cheminDocEmps, CheminDocEnterprise, cheminDocUsers, cheminPhotoEmps, cheminDocPersoEmps,
            cheminDocArticle, cheminDocMut;

    public static String getCheminArticle(YvsBaseArticles article) {
        String nameScte = article.getFamille() != null ? (article.getFamille().getSociete() != null ? article.getFamille().getSociete().getName() : "") : "";

        StringBuilder sb = new StringBuilder(USER_HOME);
        sb.append(FILE_SEPARATOR).append("lymytz");
        if (nameScte != null) {
            sb.append(FILE_SEPARATOR).append(nameScte.replace(" ", "")).append(FILE_SEPARATOR);
        }
        File file = new File(sb.toString());
        if (!file.exists()) {
            return "";

        }
        //chemin des documents d'entreprise
        cheminAllDoc = sb.append("documents").toString();
        file = new File(cheminAllDoc);
        if (!file.exists()) {
            return "";

        }
        cheminDocArticle = sb.append(FILE_SEPARATOR).append("docArticle").toString();
        file = new File(sb.toString());
        if (!file.exists()) {
            return "";
        }
        String chemin = sb.append(FILE_SEPARATOR).append(article.getRefArt()).toString();
        file = new File(chemin);
        if (!file.exists()) {
            return "";
        }
        return chemin;
    }

    public static String getCheminDocMut() {
        return cheminDocMut;
    }

    public static String getCheminMailUser(YvsMsgConversation mail) {
        StringBuilder sb = new StringBuilder(cheminDocUsers);
        File file = new File(sb.toString());
        if (!file.exists()) {
            return "";
        }
        String cheminUser = sb.append(FILE_SEPARATOR).append(mail.getEmetteur().getNomUsers().replace(" ", "_")).toString();
        file = new File(cheminUser);
        if (!file.exists()) {
            return "";
        }
        String cheminMail = sb.append(FILE_SEPARATOR).append("Mail").toString();
        file = new File(cheminMail);
        if (!file.exists()) {
            return "";
        }
        String chemin = sb.append(FILE_SEPARATOR).append(mail.getReference().replace(" ", "_")).toString();
        file = new File(chemin);
        if (!file.exists()) {
            return "";
        }
        return chemin;
    }

    public static String getCheminArticle() {
        return cheminDocArticle;
    }

    public static String getCheminLogo() {
        return cheminLogo;
    }

    public static void setCheminLogo(String cheminLogo) {
        Chemins.cheminLogo = cheminLogo;
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
}
