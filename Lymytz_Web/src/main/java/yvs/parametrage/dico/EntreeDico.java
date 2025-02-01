/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.dico;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Objects;
import yvs.BeanDeBase;

/**
 *
 * @author GOUCHERE YVES
 */
public class EntreeDico extends BeanDeBase implements Serializable {

    private String entre;
    private String symbol;

    public EntreeDico() {
    }

    public EntreeDico(String entre, String symbol) {
        this.entre = entre;
        this.symbol = symbol;
    }

    public String getEntre() {
        return entre;
    }

    public void setEntre(String entre) {
        this.entre = entre;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntreeDico other = (EntreeDico) obj;
        if (!Objects.equals(this.entre, other.entre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.entre);
        return hash;
    }
}
