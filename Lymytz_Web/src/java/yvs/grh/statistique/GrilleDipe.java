/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.statistique;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class GrilleDipe implements Serializable {

    private long id;
    private double trancheMin, trancheMax;
    private double montant;
    private ParamStatPaie base = new ParamStatPaie();

    public GrilleDipe() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTrancheMin() {
        return trancheMin;
    }

    public void setTrancheMin(double trancheMin) {
        this.trancheMin = trancheMin;
    }

    public double getTrancheMax() {
        return trancheMax;
    }

    public void setTrancheMax(double trancheMax) {
        this.trancheMax = trancheMax;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public ParamStatPaie getBase() {
        return base;
    }

    public void setBase(ParamStatPaie base) {
        this.base = base;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final GrilleDipe other = (GrilleDipe) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
