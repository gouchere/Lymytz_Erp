/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.salaire.service.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
public class Util {

    public static Date getDate(int day, int month, int year) {
        return getDateTime(day, month, year, 0, 0, 0);
    }

    public static Date getHeure(int hour, int minute, int second) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        date.set(Calendar.SECOND, second);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }

    public static Date getDateTime(int day, int month, int year, int hour, int minute, int second) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        date.set(Calendar.SECOND, second);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }

    public static Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    public static int ecartOnDate(Date dateDebut, Date dateFin, String on) {
        if (dateDebut != null && dateFin != null) {
            Calendar cal1 = dateToCalendar(dateDebut);
            Calendar cal2 = dateToCalendar(dateFin);
            Calendar cal;
            switch (on) {
                case "an":
                    int an = 0;
                    cal = cal1;
                    while (cal.before(cal2)) {
                        an++;
                        cal.add(Calendar.YEAR, 1);
                    }
                    return an;
                case "mois":
                    int mois = 0;
                    cal = cal1;
                    while (cal.before(cal2)) {
                        mois++;
                        cal.add(Calendar.MONTH, 1);
                    }
                    if (mois > 12) {
                        return 12;
                    }
                    return mois;
                case "jour":
                    int jour = 0;
                    cal = cal1;
                    while (cal.before(cal2)) {
                        jour++;
                        cal.add(Calendar.DATE, 1);
                    }
                    if (jour > 366) {
                        return 366;
                    }
                    return jour;
                case "heure":
                    int heure = 0;
                    cal = cal1;
                    while (cal.before(cal2)) {
                        heure++;
                        cal.add(Calendar.HOUR_OF_DAY, 1);
                    }
                    if (heure > 24) {
                        return 24;
                    }
                    return heure;
                default:
                    break;
            }
        }
        return 0;
    }

    public static boolean isLocalhost(String valeur) {
        return valeur != null ? (valeur.trim().equals("localhost") || valeur.trim().equals("127.0.0.1")) : false;
    }

    public static boolean asString(String valeur) {
        return valeur != null ? valeur.trim().length() > 0 : false;
    }

    public static boolean asLong(Long valeur) {
        return valeur != null ? valeur > 0 : false;
    }

    public static String nextValue(String oldValue) {
        // TODO code application logic here
        String newValue = "A";
        if (oldValue != null ? oldValue.trim().length() > 0 : false) {
            newValue = oldValue.toUpperCase();
            String temp = newValue;
            boolean continu = true;
            boolean increment = false;
            int length = newValue.length() - 1;//Recuperer la taille de la chaine entrée
            for (int i = length; i > -1; i--) {//Boucle inverse sur la taille
                char ch = newValue.charAt(i);//Recuperer le caracter a la position i
                int index = Constantes.ALPHABET.indexOf(ch);//Recupere l'index dans l'alphabet
                char rs = 'A';
                if (index == Constantes.ALPHABET.length() - 1) {//Verifie si on est a la dernier lettre de l'alphabet
                    increment = true;//On precise On va revenir a la lettre A
                } else {
                    rs = Constantes.ALPHABET.charAt(index + 1);//Recupere la lettre suivante
                    if (!continu) {//Verifie si on est passé a la lettre A
                        break;
                    }
                    if (increment) {//Definie si on est passé a la lettre A
                        continu = false;
                    }
                    index = Constantes.ALPHABET.indexOf(rs);//Recupere l'index dans l'alphabet
                    if (index != Constantes.ALPHABET.length() - 1) {//Verifie si on n'est pas a la dernier lettre de l'alphabet
                        continu = false;
                    }
                }
                newValue = newValue.substring(0, i) + String.valueOf(rs);//Change la lettre courante en sa nouvelle valeur
                if (i < length) {//Verifie si on n'est plus a la 1ere lettre a modifier
                    newValue += temp.substring(i + 1, temp.length());//Ajoute la suite des lettres a la nouvelle chaine
                }
                temp = newValue;//Sauvegarde la nouvelle valeur
            }
            increment = true;//Cas ou on a est passé a la lettre A pour toutes les lettres
            for (int i = 0; i < newValue.length(); i++) {
                if (newValue.charAt(i) != 'A') {
                    increment = false;
                    break;
                }
            }
            if (increment) {//On ajoute une nouvelle lettre A
                newValue += "A";
            }
        }
        return newValue;
    }

    public static byte[] read(File file) {
        return readStream(file).toByteArray();
    }

    public static ByteArrayOutputStream readStream(File file) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            FileInputStream fis = new FileInputStream(file);
            int c = 0;
            try {
                while ((c = fis.read()) != -1) {
                    buffer.write(c);
                    buffer.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return buffer;
    }

    public static String getTableExterne(String tableExterne) {
        switch (tableExterne) {
            case "ABONNEMENT_DIVERS":
                return "yvs_compta_abonement_doc_divers";
            case "ACOMPTE_ACHAT":
                return "yvs_compta_acompte_fournisseur";
            case "ACOMPTE_VENTE":
                return "yvs_compta_acompte_client";
            case "AVOIR_ACHAT":
            case "DOC_ACHAT":
                return "yvs_com_doc_achats";
            case "AVOIR_VENTE":
            case "DOC_VENTE":
                return "yvs_com_doc_ventes";
            case "BON_PROVISOIRE":
                return "yvs_compta_bon_provisoire";
            case "BULLETIN":
                return "yvs_grh_bulletins";
            case "CAISSE_ACHAT":
            case "CAISSE_AVOIR_ACHAT":
                return "yvs_compta_caisse_piece_achat";
            case "CAISSE_CREDIT_ACHAT":
                return "yvs_compta_reglement_credit_fournisseur";
            case "CAISSE_CREDIT_VENTE":
                return "yvs_compta_reglement_credit_client";
            case "CAISSE_VENTE":
            case "CAISSE_AVOIR_VENTE":
                return "yvs_compta_caisse_piece_vente";
            case "CAISSE_DIVERS":
                return "yvs_compta_caisse_piece_divers";
            case "CREDIT_VENTE":
                return "yvs_compta_credit_client";
            case "CREDIT_ACHAT":
                return "yvs_compta_credit_fournisseur";
            case "DOC_DIVERS":
                return "yvs_compta_caisse_doc_divers";
            case "DOC_VIREMENT":
                return "yvs_compta_caisse_piece_virement";
            case "FRAIS_MISSION":
                return "yvs_compta_caisse_piece_mission";
            case "JOURNAL_VENTE":
                return "yvs_com_entete_doc_vente";
            case "ORDRE_SALAIRE":
                return "yvs_grh_ordre_calcul_salaire";
            case "PHASE_ACOMPTE_ACHAT":
                return "yvs_compta_phase_acompte_achat";
            case "PHASE_ACOMPTE_VENTE":
                return "yvs_compta_phase_acompte_vente";
            case "PHASE_CAISSE_ACHAT":
                return "yvs_compta_phase_piece_achat";
            case "PHASE_CAISSE_DIVERS":
                return "yvs_compta_phase_piece_divers";
            case "PHASE_CAISSE_VENTE":
                return "yvs_compta_phase_piece";
            case "PHASE_CREDIT_ACHAT":
                return "yvs_compta_phase_reglement_credit_fournisseur";
            case "PHASE_CREDIT_VENTE":
                return "yvs_compta_phase_reglement_credit_client";
            case "PHASE_VIREMENT":
                return "yvs_compta_phase_piece_virement";
        }
        return "";
    }
}
