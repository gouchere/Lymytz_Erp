/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.contrat;

import java.io.Serializable;
import yvs.base.produits.UniteMesure;

/**
 *
 * @author Lymytz Dowes
 */
public class Preavis implements Serializable {

    private int id;
    private double duree;
    private UniteMesure unite = new UniteMesure();

    public Preavis() {
    }

    public Preavis(int id) {
        this.id = id;
    }

    public Preavis(int id, double duree) {
        this.id = id;
        this.duree = duree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.id;
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
        final Preavis other = (Preavis) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
