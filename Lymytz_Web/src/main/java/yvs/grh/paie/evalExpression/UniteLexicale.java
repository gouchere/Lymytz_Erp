/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie.evalExpression;

/**
 *
 * @author LYMYTZ-PC
 */
public class UniteLexicale {

    public final static int EXPRESSION = 0;
    public final static int SI = 300;
    public final static int ALORS = 301, SINON = 302, SINONSI = 303, FINSI = 304;
    public final static int ID_VAR = 350;//\$[0-9A-Za-z_\$]
    public final static int NOMBRE = 351, MOT_CLE = 352; //[0-9]+

    public final static int L_PLUS = '+', L_MOINS = '-', L_FOIS = '*', L_DIV = '/', L_PARG = '(', L_PARD = ')',
            L_SUP = '>', L_INF = '<', L_EQUALS = '=', L_ET = '&', L_OU = '|', L_EOF = -1;
    public final static char EOF = '#';

    public static final String PLUS = "+", MOINS = "-", FOIS = "*", DIV = "/";
    public static final String INF = "<", SUP = ">", EGAL = "=", ET = "&", OU = "|";

    public static boolean isANumber(String s) {
        if (s == null) {
            return false;
        }
        try {
            new java.math.BigDecimal(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
     public static double calcul(String signe, double d1, double d2) {
        switch (signe) {
            case DIV:
                return d2 / d1;
            case PLUS:
                return d2 + d1;
            case FOIS:
                return d2 * d1;
            case MOINS:
                return d2 - d1;
            default:
                return 0;
        }
    }

    public static int calculLogique(String signe, double d1, double d2) {
        switch (signe) {
            case INF:
                if (d2 < d1) {
                    return 1;
                } else {
                    return 0;
                }
            case SUP:
                if (d2 > d1) {
                    return 1;
                } else {
                    return 0;
                }
            case EGAL:
                if (d2 == d1) {
                    return 1;
                } else {
                    return 0;
                }
            case OU:
                return (int) (d1 + d2);
            case ET:
                return (int) (d1 * d2);
            default:
                return 0;
        }
    }
    
    
}
