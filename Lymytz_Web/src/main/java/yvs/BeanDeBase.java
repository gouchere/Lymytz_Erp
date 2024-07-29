/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs;

import java.io.Serializable;
import javax.ejb.EJB;
import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author GOUCHERE YVES
 */
//@ManagedBean
//@SessionScoped
public class BeanDeBase implements Serializable {

    private int numero;
    private String auteur;
    private String lastAuteur;
    private String lastDateUpdate;
    private boolean actif = true, sup;
    @EJB
    public DaoInterfaceLocal dao;

    public BeanDeBase() {
    }

    public BeanDeBase(String auteur, String lastAuteur, String lastDateUpdate, boolean sup) {
        this.auteur = auteur;
        this.lastAuteur = lastAuteur;
        this.lastDateUpdate = lastDateUpdate;
        this.sup = sup;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getLastAuteur() {
        return lastAuteur;
    }

    public void setLastAuteur(String lastAuteur) {
        this.lastAuteur = lastAuteur;
    }

    public String getLastDateUpdate() {
        return lastDateUpdate;
    }

    public void setLastDateUpdate(String lastDateUpdate) {
        this.lastDateUpdate = lastDateUpdate;
    }

    public boolean isSup() {
        return sup;
    }

    public void setSup(boolean sup) {
        this.sup = sup;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
