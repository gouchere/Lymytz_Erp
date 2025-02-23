/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rrr;

import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.util.TrancheVal;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean(name = "tarifG")
public class PlanTarifGroup {

    private String refCategorie;
    private double prix;
    private double remise;
    private TrancheVal idTranche;
    private long idCategorie;
    private long idAgence;
    private String refAgence;

    public PlanTarifGroup() {
    }

    public PlanTarifGroup(String refCategorie, String idAgence) {
        this.refCategorie = refCategorie;
        this.refAgence=idAgence;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getRefCategorie() {
        return refCategorie;
    }

    public void setRefCategorie(String refCategorie) {
        this.refCategorie = refCategorie;
    }

    public long getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(long idAgence) {
        this.idAgence = idAgence;
    }

    public void setRefAgence(String refAgence) {
        this.refAgence = refAgence;
    }

    public String getRefAgence() {
        return refAgence;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public TrancheVal getIdTranche() {
        return idTranche;
    }

    public void setIdTranche(TrancheVal idTranche) {
        this.idTranche = idTranche;
    }

    public long getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(long idCategorie) {
        this.idCategorie = idCategorie;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlanTarifGroup other = (PlanTarifGroup) obj;
        if (!Objects.equals(this.refCategorie, other.refCategorie)) {
            return false;
        }
        if (!Objects.equals(this.refAgence, other.refAgence)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.refCategorie);
        hash = 47 * hash + Objects.hashCode(this.refAgence);
        return hash;
    }
}
