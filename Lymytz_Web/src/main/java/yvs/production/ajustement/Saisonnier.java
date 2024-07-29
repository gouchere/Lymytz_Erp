/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.produits.ajustement;

import java.util.List;

/**
 *
 * @author lymytz
 */
public class Saisonnier {

    public static String[] Mois = new String[]{"Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"};

    public static Point[] ByPrecedentYear(Point[] t) {
        Point[] r = new Point[t.length];
        double som = 0;
        for (Point p : t) {
            som += p.getY();
        }
        for (int i = 0; i < r.length; i++) {
            Point p = new Point(i, t[i].getY() / som);
            r[i] = p;
        }
        return r;
    }

    public static Point[] ByMustYear(List<Point[]> l) {
        if ((l != null) ? !l.isEmpty() : false) {
            Point[] r1 = new Point[l.get(0).length];
            for (int i = 0; i < r1.length; i++) {
                double som = 0;
                for (Point[] t : l) {
                    som += t[i].getY();
                }
                Point p = new Point(i, som / l.size());
                r1[i] = p;
            }
            Point[] r = ByPrecedentYear(r1);
            return r;
        }
        return null;
    }

    public static Point[] previsionOnPeriodeByCA(Point[] t, double CA) {
        Point[] r = new Point[t.length];
        for (int i = 0; i < r.length; i++) {
            Point p = new Point(i, t[i].getY() * CA);
            r[i] = p;
        }
        return r;
    }

    public static Point[] previsionOnCAByPeriode(Point[] t, Point e) {
        return previsionOnPeriodeByCA(t, previsionCAByPeriode(t, e));
    }

    public static double previsionCAByPeriode(Point[] t, Point p) {
        return p.getY() / t[(int) p.getX() - 1].getY();
    }
}
