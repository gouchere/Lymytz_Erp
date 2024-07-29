/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean.mission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Comptes;
import yvs.entity.grh.activite.YvsGrhObjetsMissionAnalytique;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ObjetMission implements Serializable {

    private int id;
    private String titre;
    private String description;
    private Comptes compteCharge = new Comptes();
    private boolean actif;
    private Date dateSave = new Date();
    private List<YvsGrhObjetsMissionAnalytique> analytiques;

    public ObjetMission() {
        analytiques = new ArrayList<>();
    }

    public ObjetMission(int id) {
        this();
        this.id = id;
    }

    public ObjetMission(int id, String titre) {
        this(id);
        this.titre = titre;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Comptes getCompteCharge() {
        return compteCharge;
    }

    public void setCompteCharge(Comptes compteCharge) {
        this.compteCharge = compteCharge;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<YvsGrhObjetsMissionAnalytique> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsGrhObjetsMissionAnalytique> analytiques) {
        this.analytiques = analytiques;
    }

    @Override
    public String toString() {
        return "ObjetMission{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ObjetMission other = (ObjetMission) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
