/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.produits.ajustement;

import java.math.BigDecimal;

/**
 *
 * @author lymytz
 */
public class Coordonnee {

    private double a, b;

    public Coordonnee() {
    }

    public Coordonnee(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getA_() {
        BigDecimal bd = new BigDecimal(a);
        bd = bd.setScale(3, BigDecimal.ROUND_DOWN);
        a = bd.doubleValue();
        return a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getB_() {
        BigDecimal bd = new BigDecimal(b);
        bd = bd.setScale(3, BigDecimal.ROUND_DOWN);
        b = bd.doubleValue();
        return b;
    }

    @Override
    public String toString() {
        return "Coordonnee{a = " + a + ", b = " + b + '}';
    }
}
