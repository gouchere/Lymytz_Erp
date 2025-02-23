/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.depot;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Conditionnement;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ConditionnementPoint implements Serializable {

    private long id;
    private boolean actif, proportionPua;
    private double puv, tauxPua;
    private double prixMin;
    private String naturePrixMin = Constantes.NATURE_MTANT;
    private double remise;
    private String natureRemise = Constantes.NATURE_MTANT;
    private Conditionnement conditionnement = new Conditionnement();
    private ArticleDepot article = new ArticleDepot();

    public ConditionnementPoint() {
    }

    public ConditionnementPoint(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPuv() {
        return puv;
    }

    public void setPuv(double puv) {
        this.puv = puv;
    }

    public double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(double prixMin) {
        this.prixMin = prixMin;
    } 
    

    public String getNaturePrixMin() {
        return naturePrixMin != null ? (naturePrixMin.trim().length() > 0 ? naturePrixMin : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public boolean isProportionPua() {
        return proportionPua;
    }

    public void setProportionPua(boolean proportionPua) {
        this.proportionPua = proportionPua;
    }

    public double getTauxPua() {
        return tauxPua;
    }

    public void setTauxPua(double tauxPua) {
        this.tauxPua = tauxPua;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public String getNatureRemise() {
        return natureRemise != null ? (natureRemise.trim().length() > 0 ? naturePrixMin : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public ArticleDepot getArticle() {
        return article;
    }

    public void setArticle(ArticleDepot article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final ConditionnementPoint other = (ConditionnementPoint) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

}
