/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author hp Elite 8300
 */
public class Dimension2 {

    private String titre;
    private List<Valeurs> values;
    private double totalAttendu;
    private double totalRealise;

    public Dimension2() {
        values = new ArrayList<>();
    }

    public Dimension2(String titre) {
        this();
        this.titre = titre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<Valeurs> getValues() {
        return values;
    }

    public void setValues(List<Valeurs> values) {
        this.values = values;
    }

    public double getTotalAttendu() {
        return totalAttendu;
    }

    public void setTotalAttendu(double totalAttendu) {
        this.totalAttendu = totalAttendu;
    }

    public double getTotalRealise() {
        return totalRealise;
    }

    public void setTotalRealise(double totalRealise) {
        this.totalRealise = totalRealise;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.titre);
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
        final Dimension2 other = (Dimension2) obj;
        if (!Objects.equals(this.titre, other.titre)) {
            return false;
        }
        return true;
    }
}
