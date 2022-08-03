/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.statistique;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ContentDuree implements Serializable {

    private long agence;
    private long employe;
    private double valeur;  
    private String element;

    public static final String JOUR_REQUIS = "Tr";
    public static final String JOUR_EFFECTIF = "Je";
    public static final String JOUR_NORMAL = "Jn";
    public static final String JOUR_SUPPLEMENTAIRE = "Js";
    public static final String HEURE_RETARD = "Rt";
    public static final String JOUR_COMPENSATOIRE = "Jc";
    public static final String ABSENCE = "Ab";
    public static final String REPOS_EFFECTIF = "Re";
    public static final String REPOS_REQUIS = "Rr";
    public static final String CONGE_MALADIE = "Cm";
    public static final String CONGE_TECHNIQUE = "Ct";
    public static final String CONGE_ANNUEL = "Ca";
    public static final String PERMISSION_COURT_DUREE = "Pc";
    public static final String PERMISSION_LONG_DUREE = "Pl";
    public static final String FERIE = "Fe";
    public static final String MISSION = "Mi";
    public static final String FORMATION = "Fo";

    public static final String CONGE_PRINCIPAL = "CPD";
    public static final String CONGE_SUPPLEMENTAIRE = "CSD";
    public static final String CONGE_ANNUEL_PRIS = "CNP";
    public static final String PERMISSION_DU = "PLD";
    public static final String PERMISSION_LONG_SPECIAL = "PLS";
    public static final String PERMISSION_LONG_AUTORISE = "PLA";
    public static final String PERMISSION_COURT_ANNUEL = "PCN";
    public static final String PERMISSION_COURT_AUTORISE = "PCA";
    public static final String PERMISSION_COURT_SPECIAL = "PCS";
    public static final String PERMISSION_COURT_SALAIRE = "PCSL";

    public ContentDuree() {
    }

    public ContentDuree(String element) {
        this.element = element;
    }

    public ContentDuree(long employe, String element) {
        this.employe = employe;
        this.element = element;
    }

    public ContentDuree(long agence, long employe, double valeur, String element) {
        this.agence = agence;
        this.employe = employe;
        this.valeur = valeur;
        this.element = element;
    }

    public ContentDuree(Object[] data) {
        if (data != null ? data.length > 0 : false) {
            this.agence = data.length > 0 ? (data[0] != null ? (long) data[0] : 0) : 0;
            this.employe = data.length > 1 ? (data[1] != null ? (long) data[1] : 0) : 0;
            this.valeur = data.length > 2 ? (data[2] != null ? (double) data[2] : 0) : 0;
            this.element = data.length > 3 ? (data[3] != null ? (String) data[3] : "") : "";
        }
    }

    public String getCONGE_PRINCIPAL() {
        return CONGE_PRINCIPAL;
    }

    public String getCONGE_SUPPLEMENTAIRE() {
        return CONGE_SUPPLEMENTAIRE;
    }

    public String getCONGE_ANNUEL_PRIS() {
        return CONGE_ANNUEL_PRIS;
    }

    public String getPERMISSION_DU() {
        return PERMISSION_DU;
    }

    public String getPERMISSION_LONG_SPECIAL() {
        return PERMISSION_LONG_SPECIAL;
    }

    public String getPERMISSION_LONG_AUTORISE() {
        return PERMISSION_LONG_AUTORISE;
    }

    public String getPERMISSION_COURT_ANNUEL() {
        return PERMISSION_COURT_ANNUEL;
    }

    public String getPERMISSION_COURT_AUTORISE() {
        return PERMISSION_COURT_AUTORISE;
    }

    public String getJOUR_NORMAL() {
        return JOUR_NORMAL;
    }

    public String getJOUR_SUPPLEMENTAIRE() {
        return JOUR_SUPPLEMENTAIRE;
    }

    public String getHEURE_RETARD() {
        return HEURE_RETARD;
    }

    public String getJOUR_COMPENSATOIRE() {
        return JOUR_COMPENSATOIRE;
    }

    public String getREPOS_REQUIS() {
        return REPOS_REQUIS;
    }

    public String getJOUR_REQUIS() {
        return JOUR_REQUIS;
    }

    public String getJOUR_EFFECTIF() {
        return JOUR_EFFECTIF;
    }

    public String getABSENCE() {
        return ABSENCE;
    }

    public String getREPOS_EFFECTIF() {
        return REPOS_EFFECTIF;
    }

    public String getCONGE_MALADIE() {
        return CONGE_MALADIE;
    }

    public String getCONGE_TECHNIQUE() {
        return CONGE_TECHNIQUE;
    }

    public String getCONGE_ANNUEL() {
        return CONGE_ANNUEL;
    }

    public String getPERMISSION_COURT_DUREE() {
        return PERMISSION_COURT_DUREE;
    }

    public String getPERMISSION_LONG_DUREE() {
        return PERMISSION_LONG_DUREE;
    }

    public String getFERIE() {
        return FERIE;
    }

    public String getMISSION() {
        return MISSION;
    }

    public String getFORMATION() {
        return FORMATION;
    }

    public long getAgence() {
        return agence;
    }

    public void setAgence(long agence) {
        this.agence = agence;
    }

    public long getEmploye() {
        return employe;
    }

    public void setEmploye(long employe) {
        this.employe = employe;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.employe ^ (this.employe >>> 32));
        hash = 97 * hash + Objects.hashCode(this.element);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContentDuree other = (ContentDuree) obj;
        if (this.employe != other.employe) {
            return false;
        }
        if (!Objects.equals(this.element, other.element)) {
            return false;
        }
        return true;
    }

    public static String nameConstantes(String y) {
        String name = "";
        if (y != null) {
            switch (y) {
                case JOUR_REQUIS:
                    name = "Jour(s) requis";
                    break;
                case JOUR_EFFECTIF:
                    name = "Jour(s) effectif(s)";
                    break;
                case JOUR_NORMAL:
                    name = "Présence(s) normale(s)";
                    break;
                case JOUR_SUPPLEMENTAIRE:
                    name = "Présence(s) supplémentaire(s)";
                    break;
                case HEURE_RETARD:
                    name = "Retards cumulé";
                    break;
                case JOUR_COMPENSATOIRE:
                    name = "Présence(s) compensatrice(s)";
                    break;
                case ABSENCE:
                    name = "Jour(s) absent(s)";
                    break;
                case REPOS_EFFECTIF:
                    name = "Repos effectif(s)";
                    break;
                case REPOS_REQUIS:
                    name = "Repos requis";
                    break;
                case CONGE_MALADIE:
                    name = "Congé(s) maladie";
                    break;
                case CONGE_TECHNIQUE:
                    name = "Congé(s) technique";
                    break;
                case CONGE_ANNUEL:
                    name = "Congé(s) annuel";
                    break;
                case PERMISSION_COURT_DUREE:
                    name = "Permission(s) courte durée";
                    break;
                case PERMISSION_LONG_DUREE:
                    name = "Permission(s) longue durée";
                    break;
                case FERIE:
                    name = "Jour(s) Férié";
                    break;
                case MISSION:
                    name = "Mission(s) effectuée";
                    break;
                case FORMATION:
                    name = "Formation(s) effectuée";
                    break;
                case CONGE_PRINCIPAL:
                    name = "Congés principal dû";
                    break;
                case CONGE_SUPPLEMENTAIRE:
                    name = "Congés supplementaire dû";
                    break;
                case CONGE_ANNUEL_PRIS:
                    name = "Congés pris";
                    break;
                case PERMISSION_DU:
                    name = "Permissions Dû";
                    break;
                case PERMISSION_LONG_SPECIAL:
                    name = "Permissions LD spéciales";
                    break;
                case PERMISSION_LONG_AUTORISE:
                    name = "Permissions LD autorisées";
                    break;
                case PERMISSION_COURT_ANNUEL:
                    name = "Permissions CD annuelles";
                    break;
                case PERMISSION_COURT_AUTORISE:
                    name = "Permissions CD autorisées";
                    break;
                default:
                    name = y;
                    break;
            }
        }
        return name;
    }

}
