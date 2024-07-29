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
public class Lineaire {

    public static Coordonnee PointsExtremes(Point f, Point l) {
        Coordonnee r = new Coordonnee();
        r.setA((l.getY() - f.getY()) / (l.getX() - f.getX()));
        r.setB(l.getY() - (r.getA() * l.getX()));
        return r;
    }

    public static Coordonnee PointsMoyens(Point[] t) {
        Point[] G1 = new Point[t.length - t.length / 2];
        System.arraycopy(t, 0, G1, 0, G1.length);
        Point[] G2 = new Point[t.length / 2];
        for (int i = G1.length; i < t.length; i++) {
            G2[i - G1.length] = t[i];
        }
        double x = 0, y = 0;
        for (Point g : G1) {
            x += g.getX();
            y += g.getY();
        }
        x = x / G1.length;
        y = y / G1.length;
        Point Moy1 = new Point(x, y);
        x = 0;
        y = 0;
        for (Point g : G2) {
            x += g.getX();
            y += g.getY();
        }
        x = x / G2.length;
        y = y / G2.length;
        Point Moy2 = new Point(x, y);
        return PointsExtremes(Moy1, Moy2);
    }

    public static Coordonnee MoindresCarrees(Point[] t) {
        Coordonnee r = new Coordonnee();
        double somX = 0, somY = 0;
        for (Point p : t) {
            somX += p.getX();
            somY += p.getY();
        }
        double moyX = somX / t.length,
                moyY = somY / t.length;
        double somXY = 0, somXX = 0;
        for (Point p : t) {
            double diffX = p.getX() - moyX;
            double diffY = p.getY() - moyY;
            double XY = diffX * diffY;
            somXY += XY;
            double XX = diffX * diffX;
            somXX += XX;
        }
        r.setA(somXY / somXX);
        r.setB(moyY - (moyX * r.getA()));
        return r;
    }

    public static double previsionOnY(Coordonnee c, double X) {
        return (c.getA() * X) + c.getB();
    }

    public static double previsionOnY_(Coordonnee c, double X) {
        double y = (c.getA() * X) + c.getB();
        BigDecimal bd = new BigDecimal(y);
        bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
        y = bd.doubleValue();
        return y;
    }

    public static double previsionOnX(Coordonnee c, double Y) {
        return (Y - c.getB()) / c.getA();
    }

    public static double previsionOnX_(Coordonnee c, double Y) {
        double x = (Y - c.getB()) / c.getA();
        BigDecimal bd = new BigDecimal(x);
        bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
        x = bd.doubleValue();
        return x;
    }

    public static double givePrevisionY(double y1, double y2, double x1, double x2, double x) {
        //calcul le coefficient a:
        double a = (y2 - y1) / (x2 - x1);
        double b = y2 - a;
        return (x * a) + b;
    }
}
