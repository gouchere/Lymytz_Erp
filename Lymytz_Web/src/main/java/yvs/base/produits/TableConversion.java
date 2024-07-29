/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.util.Objects;

/**
 *
 * @author lymytz
 */
public class TableConversion {

    private long id;
    private double tauxChange;
    private UniteMesure uniteEquivalent = new UniteMesure();
    private UniteMesure unite = new UniteMesure();

    public TableConversion() {
    }

    public TableConversion(long id, Double tauxChange) {
        this.id = id;
        this.tauxChange = tauxChange;
    }

    public TableConversion(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTauxChange() {
        return tauxChange;
    }

    public void setTauxChange(double tauxChange) {
        this.tauxChange = tauxChange;
    }

    public UniteMesure getUniteEquivalent() {
        return uniteEquivalent;
    }

    public void setUniteEquivalent(UniteMesure uniteEquivalent) {
        this.uniteEquivalent = uniteEquivalent;
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final TableConversion other = (TableConversion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
