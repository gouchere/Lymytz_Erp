/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.production.pilotage.YvsProdOfSuiviFlux;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class SuiviOperations implements Serializable {

    private long id;
    private double coutOperation;
    private String reference, statut;
    private Date date = new Date();
    private Date heureDebut = new Date();
    private Date heureFin = new Date();
    private Date dateSave = new Date();

    private List<YvsProdOfSuiviFlux> composants;

    public SuiviOperations() {
        composants = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public double getCoutOperation() {
        return coutOperation;
    }

    public void setCoutOperation(double coutOperation) {
        this.coutOperation = coutOperation;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<YvsProdOfSuiviFlux> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdOfSuiviFlux> composants) {
        this.composants = composants;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final SuiviOperations other = (SuiviOperations) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
