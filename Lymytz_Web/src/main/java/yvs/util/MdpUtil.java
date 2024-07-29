/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GOUCHERE YVES 
 */
public class MdpUtil implements Serializable {

    public static String charArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXTZ0123456789/-*@_-()";
    public static String allCharArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXTZ0123456789&~#'{([-|`_\\^@)]=}?/§!*+µ%$£¤<>.,;:²\"àéè";

    public String ERROR = "";

    public String randomString(int length) {
        Random rd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charArray.length(); i++) {
            sb.append(charArray.charAt(rd.nextInt(70)));
        }
        return sb.toString();
    }

    public static String hashString(String mdp) {
        byte[] tabBytes = null;
        try {
            try {
                tabBytes = MessageDigest.getInstance("MD5").digest(mdp.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MdpUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MdpUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabByteToString(tabBytes);
    }

    private static String tabByteToString(byte[] tabByte) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tabByte.length; i++) {
            sb.append(tabByte[i]);
        }
        return sb.toString();
    }

    public boolean correct(String password, int lenght) {
        ERROR = "";
        if (!Util.asString(password)) {
            ERROR = "Vous devez entrer un mot de passe";
            return false;
        }
        if (password.contains(" ")) {
            ERROR = "Le mot de passe ne doit pas contenir l'espace";
            return false;
        }
        if (password.trim().length() < lenght) {
            ERROR = "Le mot de passe doit etre sur " + lenght + " caractères";
            return false;
        }
        String regex = "ABCDEFGHIJKLPMNOQRSTUVWXYZ";
        boolean contains = false;
        for (int i = 0; i < password.trim().length(); i++) {
            if (regex.contains(password.charAt(i) + "")) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            ERROR = "Le mot de passe doit contenir au moins une(01) lettre majuscule";
            return false;
        }
        regex = "0123456789";
        contains = false;
        for (int i = 0; i < password.trim().length(); i++) {
            if (regex.contains(password.charAt(i) + "")) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            ERROR = "Le mot de passe doit contenir au moins un(01) chiffre";
            return false;
        }
        regex = "&~#'{([-|`_\\^@)]=}?/§!*+µ%$£¤<>.,;:²\"àéè";
        contains = false;
        for (int i = 0; i < password.trim().length(); i++) {
            if (regex.contains(password.charAt(i) + "")) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            ERROR = "Le mot de passe doit contenir au moins un(01) caractère spécial";
            return false;
        }
        return true;
    }

    public String generedPassword(int lenght) {
        String password = "";
        
        return password;
    }
}
