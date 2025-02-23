/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class BielletagePc implements Serializable, Comparable {

    private long id;
    private String formatMonai;
    private int quantite;
    private double valeur;

    public BielletagePc() {
    }

    public BielletagePc(String formatMonai) {
        this.formatMonai = formatMonai;
    }

    public BielletagePc(long id, String formatMonai, int quantite, double valeur) {
        this.id = id;
        this.formatMonai = formatMonai;
        this.quantite = quantite;
        this.valeur = valeur;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFormatMonai() {
        return formatMonai;
    }

    public void setFormatMonai(String formatMonai) {
        this.formatMonai = formatMonai;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.formatMonai);
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
        final BielletagePc other = (BielletagePc) obj;
        if (!Objects.equals(this.formatMonai, other.formatMonai)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        BielletagePc d = (BielletagePc) o;
        return new Double(valeur).compareTo(d.valeur);
    }
}
