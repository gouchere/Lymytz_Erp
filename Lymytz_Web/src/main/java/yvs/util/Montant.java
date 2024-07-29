/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Montant implements Serializable {

    private Object other;
    private double ca;
    private double qte;

    public Montant() {
    }

    public Montant(Object other) {
        this.other = other;
    }

    public Montant(double ca, double qte) {
        this.ca = ca;
        this.qte = qte;
    }

    public Montant(Object other, double ca, double qte) {
        this.other = other;
        this.ca = ca;
        this.qte = qte;
    }

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
    }

    public double getCa() {
        return ca;
    }

    public void setCa(double ca) {
        this.ca = ca;
    }

    public double getQte() {
        return qte;
    }

    public void setQte(double qte) {
        this.qte = qte;
    }

    public double valeur(String type) {
        if (type != null) {
            switch (type) {
                case "ca":
                    return ca;
                case "qte":
                    return qte;
                default:
                    return 0;
            }
        }
        return 0;
    }
}
