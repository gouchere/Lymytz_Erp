/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.util.Date;
import yvs.base.produits.UniteMesure;

/**
 *
 * @author hp Elite 8300
 */
public class LiaisonCentres {

    private long id;
    private CentreAnalytique principal = new CentreAnalytique();
    private CentreAnalytique secondaire = new CentreAnalytique();
    private double coefficient;
    private UniteMesure unite = new UniteMesure();
    private Date dateSave = new Date();

    public LiaisonCentres() {
    }

    public CentreAnalytique getPrincipal() {
        return principal;
    }

    public void setPrincipal(CentreAnalytique principal) {
        this.principal = principal;
    }

    public CentreAnalytique getSecondaire() {
        return secondaire;
    }

    public void setSecondaire(CentreAnalytique secondaire) {
        this.secondaire = secondaire;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final LiaisonCentres other = (LiaisonCentres) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
