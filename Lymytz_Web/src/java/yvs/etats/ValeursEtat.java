/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ValeursEtat {

    private String codeLigne;
    private String libelleLigne;
    private double valeur1;
    private double valeur2;
    private String titre1;
    private String titre2;
    private double quantite1;
    private double quantite2;
    private int nombre1;
    private int nombre2;
    private int rang;

    public ValeursEtat() {
    }

    public ValeursEtat(String codeLigne, String libelleLigne, double valeur1, double valeur2, String titre1, String titre2, double quantite1, double quantite2, int nombre1, int nombre2, int rang) {
        this.codeLigne = codeLigne;
        this.libelleLigne = libelleLigne;
        this.valeur1 = valeur1;
        this.valeur2 = valeur2;
        this.titre1 = titre1;
        this.titre2 = titre2;
        this.quantite1 = quantite1;
        this.quantite2 = quantite2;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.rang = rang;
    }

    
    public String getCodeLigne() {
        return codeLigne;
    }

    public void setCodeLigne(String codeLigne) {
        this.codeLigne = codeLigne;
    }

    public String getLibelleLigne() {
        return libelleLigne;
    }

    public void setLibelleLigne(String libelleLigne) {
        this.libelleLigne = libelleLigne;
    }

    public double getValeur1() {
        return valeur1;
    }

    public void setValeur1(double valeur1) {
        this.valeur1 = valeur1;
    }

    public double getValeur2() {
        return valeur2;
    }

    public void setValeur2(double valeur2) {
        this.valeur2 = valeur2;
    }

    public String getTitre1() {
        return titre1;
    }

    public void setTitre1(String titre1) {
        this.titre1 = titre1;
    }

    public String getTitre2() {
        return titre2;
    }

    public void setTitre2(String titre2) {
        this.titre2 = titre2;
    }

    public double getQuantite1() {
        return quantite1;
    }

    public void setQuantite1(double quantite1) {
        this.quantite1 = quantite1;
    }

    public double getQuantite2() {
        return quantite2;
    }

    public void setQuantite2(double quantite2) {
        this.quantite2 = quantite2;
    }

    public int getNombre1() {
        return nombre1;
    }

    public void setNombre1(int nombre1) {
        this.nombre1 = nombre1;
    }

    public int getNombre2() {
        return nombre2;
    }

    public void setNombre2(int nombre2) {
        this.nombre2 = nombre2;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

}
