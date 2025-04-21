/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.contrat;

/**
 *
 * @author hp Elite 8300
 */
public class GrilleTauxFinContrat {

    private long id;
    private int ancMin, ancMax;
    private double taux;

    public GrilleTauxFinContrat() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAncMin() {
        return ancMin;
    }

    public void setAncMin(int ancMin) {
        this.ancMin = ancMin;
    }

    public int getAncMax() {
        return ancMax;
    }

    public void setAncMax(int ancMax) {
        this.ancMax = ancMax;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final GrilleTauxFinContrat other = (GrilleTauxFinContrat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
