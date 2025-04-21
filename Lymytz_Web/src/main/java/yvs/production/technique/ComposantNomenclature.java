/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ComposantNomenclature implements Serializable {

    private long id;
    private Articles article = new Articles();
    private Nomenclature nomenclature = new Nomenclature();
    private double quantite;
    private double coefficient=1; //coefficient de convertion de l'unité du composant vers celle du composé
    private int niveau, ordre = 1;
    private String modeArrondi;
    private String typeComposant;
    private Date dateDebut = new Date(), dateFin = new Date();
    private boolean selectActif, alwayValid, actif = true;
    private boolean stockable, insideCout;
    private Conditionnement unite = new Conditionnement();
    private int numeroOperation;    //numéro devant correspondre à un numéro d'opération existant dans la gamme du même article existant
    private Date dateSave = new Date();
    private boolean composantLie = false;
    private boolean alternatif = false;
    private boolean freeUse = false;

    public ComposantNomenclature() {
        this.typeComposant = "N";
        this.modeArrondi = "E";
        this.coefficient=1;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public boolean isAlwayValid() {
        return alwayValid;
    }

    public void setAlwayValid(boolean alwayValid) {
        this.alwayValid = alwayValid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isInsideCout() {
        return insideCout;
    }

    public void setInsideCout(boolean insideCout) {
        this.insideCout = insideCout;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public String getTypeComposant() {
        return typeComposant != null ? typeComposant.trim().length() > 0 ? typeComposant : "N" : "N";
    }

    public void setTypeComposant(String typeComposant) {
        this.typeComposant = typeComposant;
    }

    public String getModeArrondi() {
        return modeArrondi != null ? modeArrondi.trim().length() > 0 ? modeArrondi : "E" : "E";
    }

    public void setModeArrondi(String modeArrondi) {
        this.modeArrondi = modeArrondi;
    }

    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : new Date();
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin != null ? dateFin : new Date();
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Conditionnement getUnite() {
        return unite;
    }

    public void setUnite(Conditionnement unite) {
        this.unite = unite;
    }

    public Nomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(Nomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public int getNumeroOperation() {
        return numeroOperation;
    }

    public void setNumeroOperation(int numeroOperation) {
        this.numeroOperation = numeroOperation;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isStockable() {
        return stockable;
    }

    public void setStockable(boolean stockable) {
        this.stockable = stockable;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public boolean isComposantLie() {
        return composantLie;
    }

    public void setComposantLie(boolean composantLie) {
        this.composantLie = composantLie;
    }

    public boolean isAlternatif() {
        return alternatif;
    }

    public void setAlternatif(boolean alternatif) {
        this.alternatif = alternatif;
    }

    public boolean isFreeUse() {
        return freeUse;
    }

    public void setFreeUse(boolean freeUse) {
        this.freeUse = freeUse;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ComposantNomenclature other = (ComposantNomenclature) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
