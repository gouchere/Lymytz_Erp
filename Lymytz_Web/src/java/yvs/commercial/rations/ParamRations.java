/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.entity.commercial.ration.YvsComParamRationSuspension;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ParamRations implements Serializable {

    private long id;
    private Articles article = new Articles();
    private Conditionnement conditionement = new Conditionnement();
    private PersonnelRation personnel = new PersonnelRation();
    private int periode = 30;
    private Date datePriseEffet = new Date();
    private double quantite;
    private boolean actif = true;
    private boolean proportionnel;
    private List<YvsComParamRationSuspension> suspensions;

    public ParamRations() {
        suspensions = new ArrayList<>();
    }

    public ParamRations(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPeriode() {
        return periode;
    }

    public void setPeriode(int periode) {
        this.periode = periode;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public PersonnelRation getPersonnel() {
        return personnel;
    }

    public void setPersonnel(PersonnelRation personnel) {
        this.personnel = personnel;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Date getDatePriseEffet() {
        return datePriseEffet != null ? datePriseEffet : new Date();
    }

    public void setDatePriseEffet(Date datePriseEffet) {
        this.datePriseEffet = datePriseEffet;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Conditionnement getConditionement() {
        return conditionement;
    }

    public void setConditionement(Conditionnement conditionement) {
        this.conditionement = conditionement;
    }

    public boolean isProportionnel() {
        return proportionnel;
    }

    public void setProportionnel(boolean proportionnel) {
        this.proportionnel = proportionnel;
    }

    public boolean isSuspendu() {
        return suspensions != null ? !suspensions.isEmpty() : false;
    }

    public List<YvsComParamRationSuspension> getSuspensions() {
        return suspensions;
    }

    public void setSuspensions(List<YvsComParamRationSuspension> suspensions) {
        this.suspensions = suspensions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ParamRations other = (ParamRations) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
