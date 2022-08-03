/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.production.base;

import yvs.production.technique.PosteCharge;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.produits.YvsBaseArticles;
import yvs.parametrage.agence.Agence;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class SiteProduction implements Serializable {
    private int id;
    private String reference;
    private String designation;
    private String description;
    private String adresse;
    private List<PosteCharge> PosteChargeList;    
    private List<CentreCharge> CentreChargeList;
    private List<YvsBaseArticles> articles;
    private Agence agence = new Agence();
    private boolean selectActif;

    public SiteProduction() {
        CentreChargeList = new ArrayList<>();
        PosteChargeList = new ArrayList<>();
        articles = new ArrayList<>();
    }

    public SiteProduction(int id) {
        this();
        this.id = id;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<PosteCharge> getPosteChargeList() {
        return PosteChargeList;
    }

    public void setPosteChargeList(List<PosteCharge> PosteChargeList) {
        this.PosteChargeList = PosteChargeList;
    }

    public List<CentreCharge> getCentreChargeList() {
        return CentreChargeList;
    }

    public void setCentreChargeList(List<CentreCharge> CentreChargeList) {
        this.CentreChargeList = CentreChargeList;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.id;
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
        final SiteProduction other = (SiteProduction) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
