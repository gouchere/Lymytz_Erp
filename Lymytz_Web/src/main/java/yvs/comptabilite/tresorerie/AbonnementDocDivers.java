/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.comptabilite.caisse.PlanDecoupage;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class AbonnementDocDivers implements Serializable {

    private long id;
    private Date dateSave = new Date();
    private Date echeance = new Date();
    private double valeur;
    private boolean actif, comptabilise;
    private String libelle;
    private DocCaissesDivers docDivers = new DocCaissesDivers();
    private PlanDecoupage plan = new PlanDecoupage();

    public AbonnementDocDivers() {
    }

    public AbonnementDocDivers(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getEcheance() {
        return echeance;
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public DocCaissesDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(DocCaissesDivers docDivers) {
        this.docDivers = docDivers;
    }

    public PlanDecoupage getPlan() {
        return plan;
    }

    public void setPlan(PlanDecoupage plan) {
        this.plan = plan;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final AbonnementDocDivers other = (AbonnementDocDivers) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
