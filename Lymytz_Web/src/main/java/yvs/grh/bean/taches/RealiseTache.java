/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean.taches;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.taches.YvsGrhTacheEmps;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class RealiseTache implements Serializable {

    private long id;
    private Date dateRealisation, debutRealisation, finRealisation;
    private double pourcentageValidation=100, quantite;
    private char etatRealisation='R';
    private YvsGrhTacheEmps tacheEmploye = new YvsGrhTacheEmps();

    public RealiseTache() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateRealisation() {
        return dateRealisation;
    }

    public void setDateRealisation(Date dateRealisation) {
        this.dateRealisation = dateRealisation;
    }

    public Date getDebutRealisation() {
        return debutRealisation;
    }

    public void setDebutRealisation(Date debutRealisation) {
        this.debutRealisation = debutRealisation;
    }

    public Date getFinRealisation() {
        return finRealisation;
    }

    public void setFinRealisation(Date finRealisation) {
        this.finRealisation = finRealisation;
    }

    public double getPourcentageValidation() {
        return pourcentageValidation;
    }

    public void setPourcentageValidation(double pourcentageValidation) {
        this.pourcentageValidation = pourcentageValidation;
    }

    public char getEtatRealisation() {
        return etatRealisation;
    }

    public void setEtatRealisation(char etatRealisation) {
        this.etatRealisation = etatRealisation;
    }

    public YvsGrhTacheEmps getTacheEmploye() {
        return tacheEmploye;
    }

    public void setTacheEmploye(YvsGrhTacheEmps tacheEmploye) {
        this.tacheEmploye = tacheEmploye;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getQuantite() {
        return quantite;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final RealiseTache other = (RealiseTache) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
