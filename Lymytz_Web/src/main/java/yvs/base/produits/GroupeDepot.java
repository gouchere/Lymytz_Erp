/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "groupDepot")
@SessionScoped
public class GroupeDepot implements Serializable {

    private String refDepot;
    private long idDepot;
    private String modeAppro;
    private String modeReappro;
    private double stockMax;
    private double stockMin;
    private int nombreJour;

    public GroupeDepot() {
    }

    public GroupeDepot(String refDepot) {
        this.refDepot = refDepot;
    }

    public long getIdDepot() {
        return idDepot;
    }

    public void setIdDepot(long idDepot) {
        this.idDepot = idDepot;
    }

    public String getModeAppro() {
        return modeAppro;
    }

    public void setModeAppro(String modeAppro) {
        this.modeAppro = modeAppro;
    }

    public String getModeReappro() {
        return modeReappro;
    }

    public void setModeReappro(String modeReappro) {
        this.modeReappro = modeReappro;
    }

    public int getNombreJour() {
        return nombreJour;
    }

    public void setNombreJour(int nombreJour) {
        this.nombreJour = nombreJour;
    }

    public String getRefDepot() {
        return refDepot;
    }

    public void setRefDepot(String refDepot) {
        this.refDepot = refDepot;
    }

    public double getStockMax() {
        return stockMax;
    }

    public void setStockMax(double stockMax) {
        this.stockMax = stockMax;
    }

    public double getStockMin() {
        return stockMin;
    }

    public void setStockMin(double stockMin) {
        this.stockMin = stockMin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GroupeDepot other = (GroupeDepot) obj;
        if (!Objects.equals(this.refDepot, other.refDepot)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.refDepot);
        return hash;
    }
}
