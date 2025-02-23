/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.commission;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean; 
import javax.faces.bean.SessionScoped;
import yvs.entity.commercial.commission.YvsComFacteurTaux;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PlanCommission implements Serializable {

    private long id;
    private String reference, intitule;
    private boolean actif = true;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private List<YvsComFacteurTaux> facteurs;

    public PlanCommission() {
        facteurs = new ArrayList<>();
    }

    public PlanCommission(long id) {
        this();
        this.id = id;
    }

    public PlanCommission(long id, String reference) {
        this(id);
        this.reference = reference;
    }

    public PlanCommission(long id, String reference, String intitule) {
        this(id, reference);
        this.intitule = intitule;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<YvsComFacteurTaux> getFacteurs() {
        return facteurs;
    }

    public void setFacteurs(List<YvsComFacteurTaux> facteurs) {
        this.facteurs = facteurs;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PlanCommission other = (PlanCommission) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
