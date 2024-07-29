/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.util.Date;

/**
 *
 * @author hp Elite 8300
 */
public class DataModele {

    private Object valeur;
    private int jour;
    private int typeValeur;
    private Date date = new Date();

    public DataModele() {
    }

    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    public int getTypeValeur() {
        return typeValeur;
    }

    public void setTypeValeur(int typeValeur) {
        this.typeValeur = typeValeur;
    }

    public int getJour() {
        return jour;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
