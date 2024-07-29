/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rrr;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class GrilleRabais implements Serializable, Comparable {

    private long id;
    private double montantMaximal;
    private double montantMinimal;
    private double montantRabais;
    private double valeur;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private String natureMontant = Constantes.NATURE_TAUX;
    private String base = Constantes.BASE_CATTC;
    private Articles article = new Articles();
    private Conditionnement conditionnement = new Conditionnement();
    private long parent;
    private String reference;
    private boolean selectActif, new_, unique = true, update;

    public GrilleRabais() {
    }

    public GrilleRabais(long id) {
        this.id = id;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public String getBase() {
        return base != null ? base.trim().length() > 0 ? base : Constantes.BASE_CATTC : Constantes.BASE_CATTC;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontantMaximal() {
        return montantMaximal;
    }

    public void setMontantMaximal(double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public double getMontantMinimal() {
        return montantMinimal;
    }

    public void setMontantMinimal(double montantMinimal) {
        this.montantMinimal = montantMinimal;
    }

    public double getMontantRabais() {
        return montantRabais;
    }

    public void setMontantRabais(double montantRabais) {
        this.montantRabais = montantRabais;
    }

    public String getNatureMontant() {
        return natureMontant != null ? natureMontant.trim().length() > 0 ? natureMontant : String.valueOf(Constantes.NATURE_MTANT) : String.valueOf(Constantes.NATURE_MTANT);
    }

    public void setNatureMontant(String natureMontant) {
        this.natureMontant = natureMontant;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final GrilleRabais other = (GrilleRabais) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        GrilleRabais g = (GrilleRabais) o;
        if (montantMinimal == g.montantMinimal) {
            if (montantMaximal == g.montantMaximal) {
                return Long.valueOf(id).compareTo(g.id);
            }
            return Double.valueOf(montantMaximal).compareTo(g.montantMaximal);
        }
        return Double.valueOf(montantMinimal).compareTo(g.montantMinimal);
    }

}
