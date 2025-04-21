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

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class CentreCharge implements Serializable {

    private int id;
    private String reference;
    private String designation;
    private String description;
    private boolean actif;
    private List<PosteCharge> PosteChargeList;
    private SiteProduction siteProduction = new SiteProduction();

    public CentreCharge() {
        PosteChargeList = new ArrayList<>();
    }

    public CentreCharge(int id) {
        this.id = id;
        PosteChargeList = new ArrayList<>();
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

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<PosteCharge> getPosteChargeList() {
        return PosteChargeList;
    }

    public void setPosteChargeList(List<PosteCharge> PosteChargeList) {
        this.PosteChargeList = PosteChargeList;
    }

    public SiteProduction getSiteProduction() {
        return siteProduction;
    }

    public void setSiteProduction(SiteProduction siteProduction) {
        this.siteProduction = siteProduction;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.id;
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
        final CentreCharge other = (CentreCharge) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
