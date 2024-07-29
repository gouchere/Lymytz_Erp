/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie.noteDeFrais;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.Employe;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class NoteDeFrais implements Serializable {

    private long id;
    private String description, statut;
    private Date date = new Date();
    private Employe employe = new Employe();
    private CentreDepense centreAnal = new CentreDepense();
    private List<Depenses> depenses;
    private double totalMontant, totalMontantApprouve;
    private boolean selectActif;

    public NoteDeFrais() {
        depenses = new ArrayList<>();
    }

    public NoteDeFrais(long id) {
        this.id = id;
        depenses = new ArrayList<>();
    }

    public double getTotalMontant() {
        return totalMontant;
    }

    public void setTotalMontant(double totalMontant) {
        this.totalMontant = totalMontant;
    }

    public double getTotalMontantApprouve() {
        return totalMontantApprouve;
    }

    public void setTotalMontantApprouve(double totalMontantApprouve) {
        this.totalMontantApprouve = totalMontantApprouve;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public CentreDepense getCentreAnal() {
        return centreAnal;
    }

    public void setCentreAnal(CentreDepense centreAnal) {
        this.centreAnal = centreAnal;
    }

    public List<Depenses> getDepenses() {
        return depenses;
    }

    public void setDepenses(List<Depenses> depenses) {
        this.depenses = depenses;
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
        final NoteDeFrais other = (NoteDeFrais) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
