/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author hp Elite 8300
 */
public class ParamStocks {

    private Date date;
    private double quantite;

    public ParamStocks() {
    }

    public ParamStocks(Date date, double quantite) {
        this.date = date;
        this.quantite = quantite;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.date);
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
        final ParamStocks other = (ParamStocks) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }

}
