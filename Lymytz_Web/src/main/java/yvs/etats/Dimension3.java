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
public class Dimension3 {

    private String titre;
    private List<Dimension2> values;

    public Dimension3() {
        values = new ArrayList<>();
    }

    public Dimension3(String titre) {
        this();
        this.titre = titre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<Dimension2> getValues() {
        return values;
    }

    public void setValues(List<Dimension2> values) {
        this.values = values;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.titre);
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
        final Dimension3 other = (Dimension3) obj;
        if (!Objects.equals(this.titre, other.titre)) {
            return false;
        }
        return true;
    }
    
    

}
