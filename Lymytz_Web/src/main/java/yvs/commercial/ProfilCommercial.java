/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.tiers.Tiers;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ProfilCommercial implements Serializable {

    private String type = "LIVREUR";
    private String modelDepot = "DPT-", modelCaisse = "C-", modelPoint = "PV-";
    private boolean generedTiers = true, generedCaisse = true, generedPlanning = true, generedCommercial = true, generedDepot = true, generedPoint = true;
    private long trancheDepot, tranchePoint, templateTiers, users;
    private Tiers tiers = new Tiers();
    private boolean skip;

    public ProfilCommercial() {
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTemplateTiers() {
        return templateTiers;
    }

    public void setTemplateTiers(long templateTiers) {
        this.templateTiers = templateTiers;
    }

    public String getModelCaisse() {
        return modelCaisse;
    }

    public void setModelCaisse(String modelCaisse) {
        this.modelCaisse = modelCaisse;
    }

    public boolean isGeneredTiers() {
        return generedTiers;
    }

    public void setGeneredTiers(boolean generedTiers) {
        this.generedTiers = generedTiers;
    }

    public boolean isGeneredCaisse() {
        return generedCaisse;
    }

    public void setGeneredCaisse(boolean generedCaisse) {
        this.generedCaisse = generedCaisse;
    }

    public boolean isGeneredPlanning() {
        return generedPlanning;
    }

    public void setGeneredPlanning(boolean generedPlanning) {
        this.generedPlanning = generedPlanning;
    }

    public boolean isGeneredCommercial() {
        return generedCommercial;
    }

    public void setGeneredCommercial(boolean generedCommercial) {
        this.generedCommercial = generedCommercial;
    }

    public String getModelDepot() {
        return modelDepot;
    }

    public void setModelDepot(String modelDepot) {
        this.modelDepot = modelDepot;
    }

    public String getModelPoint() {
        return modelPoint;
    }

    public void setModelPoint(String modelPoint) {
        this.modelPoint = modelPoint;
    }

    public boolean isGeneredDepot() {
        return generedDepot;
    }

    public void setGeneredDepot(boolean generedDepot) {
        this.generedDepot = generedDepot;
    }

    public boolean isGeneredPoint() {
        return generedPoint;
    }

    public void setGeneredPoint(boolean generedPoint) {
        this.generedPoint = generedPoint;
    }

    public long getTrancheDepot() {
        return trancheDepot;
    }

    public void setTrancheDepot(long trancheDepot) {
        this.trancheDepot = trancheDepot;
    }

    public long getTranchePoint() {
        return tranchePoint;
    }

    public void setTranchePoint(long tranchePoint) {
        this.tranchePoint = tranchePoint;
    }

    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.type);
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
        final ProfilCommercial other = (ProfilCommercial) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

}
