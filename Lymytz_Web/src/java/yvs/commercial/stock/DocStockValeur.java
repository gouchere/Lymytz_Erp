/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class DocStockValeur implements Serializable {

    private long id;
    private double montant;
    private double coefficient = 1;
    private String valoriseMpBy = "A";
    private String valorisePfBy = "V";
    private String valorisePfsBy = "R";
    private String valoriseMsBy = "V";
    private Date dateSave = new Date();

    public DocStockValeur() {
    }

    public DocStockValeur(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public String getValoriseMpBy() {
        return valoriseMpBy;
    }

    public void setValoriseMpBy(String valoriseMpBy) {
        this.valoriseMpBy = valoriseMpBy;
    }

    public String getValorisePfBy() {
        return valorisePfBy;
    }

    public void setValorisePfBy(String valorisePfBy) {
        this.valorisePfBy = valorisePfBy;
    }

    public String getValorisePfsBy() {
        return valorisePfsBy;
    }

    public void setValorisePfsBy(String valorisePfsBy) {
        this.valorisePfsBy = valorisePfsBy;
    }

    public String getValoriseMsBy() {
        return valoriseMsBy;
    }

    public void setValoriseMsBy(String valoriseMsBy) {
        this.valoriseMsBy = valoriseMsBy;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DocStockValeur other = (DocStockValeur) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
