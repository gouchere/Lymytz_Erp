/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.util.TrancheVal;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean(name = "tarifClt")
public class PlanTarifClient {

    private String refArticle;
    private String groupe;
    private double prix;
    private double remise;
    private TrancheVal idTranche;
    private long idArticle;

    public PlanTarifClient() {
    }

    public PlanTarifClient(String refArt) {
        this.refArticle = refArt;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(long idArticle) {
        this.idArticle = idArticle;
    }

    public String getRefArticle() {
        return refArticle;
    }

    public void setRefArticle(String refArticle) {
        this.refArticle = refArticle;
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

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlanTarifClient other = (PlanTarifClient) obj;
        if (!Objects.equals(this.refArticle, other.refArticle)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.refArticle);
        return hash;
    }
}
