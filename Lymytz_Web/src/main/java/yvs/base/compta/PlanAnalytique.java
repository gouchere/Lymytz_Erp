/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;

/**
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class PlanAnalytique implements Serializable {

    private long id;
    private String codePlan;
    private String intitule;
    private String description;
    private List<YvsComptaCentreAnalytique> centres;
    private boolean actif = true;
    private Date dateSave = new Date();

    public PlanAnalytique() {
        centres = new ArrayList<>();
    }

    public PlanAnalytique(long id) {
        this();
        this.id = id;
    }

    public PlanAnalytique(long id, String intitule, String description) {
        this(id);
        this.intitule = intitule;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<YvsComptaCentreAnalytique> getCentres() {
        return centres;
    }

    public void setCentres(List<YvsComptaCentreAnalytique> centres) {
        this.centres = centres;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getCodePlan() {
        return codePlan;
    }

    public void setCodePlan(String codePlan) {
        this.codePlan = codePlan;
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
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PlanAnalytique other = (PlanAnalytique) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
