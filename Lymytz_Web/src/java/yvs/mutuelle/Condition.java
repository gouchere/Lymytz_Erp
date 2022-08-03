/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.Comparator;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Condition implements Serializable, Comparator<Condition> {

    private long id;
    private String libelle, code;
    private double valeurRequise, valeurRequise2, valeurEntree;
    private boolean correct;
    private String commentaire;
    private Date dateModification;
    private Date dateSave = new Date();
    private String unite;

    public Condition() {
    }

    public Condition(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Condition(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Condition(int id, String libelle, boolean correct) {
        this.id = id;
        this.libelle = libelle;
        this.correct = correct;
    }

    public Condition(long id, String libelle, double valeur, double entree, String unite, boolean correct) {
        this.id = id;
        this.libelle = libelle;
        this.valeurRequise = valeur;
        this.valeurEntree = entree;
        this.correct = correct;
        this.unite = unite;
    }

    public Condition(long id, String code, String libelle, double valeur, double entree, String unite, boolean correct) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.valeurRequise = valeur;
        this.valeurEntree = entree;
        this.correct = correct;
        this.unite = unite;
    }

    public Condition(long id, String code, String libelle, double valeur, double valeur2, double entree, String unite, boolean correct) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.valeurRequise = valeur;
        this.valeurEntree = entree;
        this.correct = correct;
        this.unite = unite;
        this.valeurRequise2 = valeur2;
    }

    public Condition(Condition c) {
        this.id = c.id;
        this.code = c.code;
        this.libelle = c.libelle;
        this.valeurRequise = c.valeurRequise;
        this.valeurEntree = c.valeurEntree;
        this.correct = c.correct;
        this.unite = c.unite;
        this.dateModification = c.dateModification;
        this.valeurRequise2 = c.getValeurRequise2();
    }

    public void setValeurRequise2(double valeurRequise2) {
        this.valeurRequise2 = valeurRequise2;
    }

    public double getValeurRequise2() {
        return valeurRequise2;
    }

    public double getValeurRequise() {
        return valeurRequise;
    }

    public void setValeurRequise(double valeurRequise) {
        this.valeurRequise = valeurRequise;
    }

    public double getValeurEntree() {
        return valeurEntree;
    }

    public void setValeurEntree(double valeurEntree) {
        this.valeurEntree = valeurEntree;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Condition other = (Condition) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(Condition o1, Condition o2) {
        if (o1.getCode() == null) {
            return -1;
        } else if (o2.getCode() == null) {
            return 1;
        } else if (o1.getCode().compareTo(o2.getCode()) > 1) {
            return 1;
        } else if (o1.getCode().compareTo(o2.getCode()) < 1) {
            return -1;
        } else {
            return 0;
        }
    }

}
