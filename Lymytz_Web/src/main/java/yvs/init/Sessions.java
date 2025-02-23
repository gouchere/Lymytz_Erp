/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.init;

import java.util.Objects;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Lymytz
 */
public class Sessions {

    private String id;
    private HttpSession valeur;

    public Sessions() {
    }

    public Sessions(String id) {
        this.id = id;
    }

    public Sessions(String id, HttpSession valeur) {
        this.id = id;
        this.valeur = valeur;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HttpSession getValeur() {
        return valeur;
    }

    public void setValeur(HttpSession valeur) {
        this.valeur = valeur;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Sessions other = (Sessions) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Sessions{" + "id=" + id + ", valeur=" + valeur + '}';
    }

}
