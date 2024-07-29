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
public class Point {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getX_() {
        BigDecimal bd = new BigDecimal(x);
        bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
        x = bd.doubleValue();
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public double getY_() {
        BigDecimal bd = new BigDecimal(y);
        bd = bd.setScale(3, BigDecimal.ROUND_DOWN);
        y = bd.doubleValue();
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
