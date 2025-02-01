/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Lymytz-pc
 */
public class Reference implements Serializable {

    private String prefixe;
    private String current;

    public Reference(String prefixe) {
        this.prefixe = prefixe;
    }

    public Reference(String prefixe, String current) {
        this(prefixe);
        this.current = current;
    }

    public String getPrefixe() {
        return prefixe;
    }

    public void setPrefixe(String prefixe) {
        this.prefixe = prefixe;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String get() {
        return prefixe.concat(current);
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Reference other = (Reference) obj;
        if (!Objects.equals(this.prefixe, other.prefixe)) {
            return false;
        }
        return true;
    }

}
