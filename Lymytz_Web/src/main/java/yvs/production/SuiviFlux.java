/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.presence.TrancheHoraire;
import yvs.production.base.EquipeProduction;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class SuiviFlux implements Serializable {

    private long id;
    private double quantite;
    private double quantitePerdue;
    private boolean calculPr;
    private Date date = new Date();
    private Date heureDebut = new Date();
    private Date heureFin = new Date();
    private Date dateSave = new Date();
    private EquipeProduction equipe = new EquipeProduction();
    private TrancheHoraire tranche = new TrancheHoraire();

    public SuiviFlux() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EquipeProduction getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeProduction equipe) {
        this.equipe = equipe;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public double getQuantitePerdue() {
        return quantitePerdue;
    }

    public void setQuantitePerdue(double quantitePerdue) {
        this.quantitePerdue = quantitePerdue;
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
        final SuiviFlux other = (SuiviFlux) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
