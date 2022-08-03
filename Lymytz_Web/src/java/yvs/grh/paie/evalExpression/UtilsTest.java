/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie.evalExpression;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhElementStructure;
import yvs.util.Constantes;

/**
 *
 * @author LYMYTZ-PC garde une liste de lexème pour les test
 */
@ManagedBean
@SessionScoped
public class UtilsTest implements Serializable {

    @EJB
    public static DaoInterfaceLocal dao;

    public static List<Lexemes> liste;
    public static Map<String, Integer> tabMotCle = new HashMap<>();

    public UtilsTest() {
        /**
         * liste = new ArrayList<>(); Lexemes l = new Lexemes(); l.nature = 350;
         * l.valeur = 15; l.nom = "V1"; liste.add(l); l = new Lexemes();
         * l.nature = 350; l.valeur = 2; l.nom = "V2"; liste.add(l); l = new
         * Lexemes(); l.nature = 350; l.expression = "V1*V2"; l.nom = "V3";
         * liste.add(l); l = new Lexemes(); l.nature = 350; l.expression =
         * "V2/V1"; l.nom = "V4"; liste.add(l); l = new Lexemes(); l.nature =
         * 350; l.expression = "18*V1"; l.nom = "V5"; liste.add(l); l = new
         * Lexemes(); l.nature = 350; l.expression = "(V1*15)+V7"; l.nom = "V6";
         * liste.add(l); l = new Lexemes(); l.nature = 350; l.expression = "V8";
         * l.nom = "V7"; liste.add(l); l = new Lexemes(); l.nature = 350;
         * l.expression = "(12*V1)+V3"; l.nom = "V8"; liste.add(l);
         *
         */

        tabMotCle.put("SI", UniteLexicale.SI);
        tabMotCle.put("ALORS", UniteLexicale.ALORS);
        tabMotCle.put("SINON", UniteLexicale.SINON);
        tabMotCle.put("SINONSI", UniteLexicale.SINON);
        tabMotCle.put("FINSI", UniteLexicale.FINSI);
    }

    public static Lexemes buildLexeme(YvsGrhElementSalaire e) {
        Lexemes lx = new Lexemes();
        lx.designation = e.getNom();
        lx.code = e.getCode();
        lx.nature = 351;
        switch (e.getTypeMontant()) {
            case 1://montant fixe
                lx.expression = null;
                lx.nom = "" + e.getMontant();
                lx.valeur = e.getMontant().intValue();
                break;
            case 2://pourcetage
                lx.expression = "(" + (e.getMontant() / 100) + ")*" + e.getBasePourcentage().getCode();
                lx.nom = e.getCode();
                lx.valeur = 0;
                break;
            case 3://expression
                lx.expression = extractExpression(e.getExpressionRegle());
                lx.nom = e.getCode();
                lx.valeur = 0;
                break;
            case 4://quantité
                lx.expression = e.getExpressionRegle();
                lx.nom = e.getCode();
                lx.valeur = 0;
                break;
            default://elément de salaire système (0)
                lx.nom = e.getCode();
                lx.valeur = 0;
                lx.systeme = true;
                break;
        }
        return lx;
    }

    public static List<Lexemes> buildLexeme(List<YvsGrhElementStructure> l) {
        List<Lexemes> result = new ArrayList<>();
        for (YvsGrhElementStructure e : l) {
            Lexemes lx = buildLexeme(e.getElement());
            if (lx != null) {
                result.add(lx);
            }
        }
        return result;
    }

    /**
     * *
     * @param expression
     * @return
     */
    /*cette méthode, permet d'extraire l'expression algébrique d'une règle de salaire.
     *elle trouve son sens dans les cas où l'expression algébrique à évaluer est donnée sous forme conditionnelle.
     */
    public static String extractExpression(String expression) {
        String resultat = null;
        if (!expression.startsWith("SI")) {
            //l'expression n'est pas conditionnelle
            resultat = expression;
        } else {
            //1. découpe le texte en bloc délimité par le caractère ";"
            String[] tabExp = expression.split(";");
            resultat = "";
            int i = 0;
            //ce tableau contient les conditions et les expressions correspondantes
            String tabExpC[][] = new String[tabExp.length][2];
            for (String str : tabExp) {
                String[] tab = str.trim().split(":");
                for (String s : tab) {
                    System.err.println(i + " " + s.trim());
                }
                tabExpC[i][0] = tab[0];
                tabExpC[i][1] = tab[1];
                i++;
            }
//            extractExpression(tabExpC);
        }
        return resultat;
    }

    public static String rewriteExpression(List<Lexemes> l) {
        String newExpression = "";
        //verifie que le lexème se trouve dans la liste des identifiants enregistrée.
        int i = 0;
        for (Lexemes lx : l) {
            if (lx.nature == UniteLexicale.ID_VAR) {
                //recherche la valeur.
                String str = findVar(lx);
                if (str == null) {
                    return null;
                }
                newExpression += "" + str;
            } else if (lx.nature == UniteLexicale.MOT_CLE) {
                newExpression += "";
            } else {
                newExpression += " " + lx.nom;
            }
            i++;
        }
        return newExpression;
    }

    // recherche dans la liste des élément l'élément d'id spécifié et retourner sa valeur ou son expression
    private static String findVar(Lexemes lxVar) {
        String re = "";
        String[] champ = new String[]{"code"};
        Object[] val = new Object[]{lxVar.code};
        YvsGrhElementSalaire elt = (YvsGrhElementSalaire) dao.loadOneByNameQueries("YvsGrhElementSalaire.findByCode", champ, val);
        if (elt != null) {
            Lexemes lx = UtilsTest.buildLexeme(elt);
            if (!lx.systeme) {
                if (lx.valeur != 0) {
                    re = " " + lx.valeur;
                } else {
                    //si la chaine retourné est une expression, on la reconvertie en Liste de lexème puis on la retransmet
                    FactoryLexmes bl = new FactoryLexmes(lx.expression);
                    List<Lexemes> tab = new ArrayList<>();
                    Lexemes newL;
//                    while ((newL = bl.suivant()) != null) {
//                        tab.add(newL);
//                    }
                    re = " (" + rewriteExpression(tab) + " )";
//            re = "(" + lx.expression + ")";
                }
            } else {
                //évalue les éléments de salaire par défaut
                re = " " + calculEltSalaire(lx.code);
            }
        } else {
            //ereur
            return null;
        }

        return re;
    }

    private static double calculEltSalaire(String code) {
        double result = 0;
        switch (code) {
//            case Constantes.S_HEURE_DE_TRAVAIL:
////                result = getHeureTravail();
//                System.err.println("Heure de travail ---" + result);
//                break;
            case Constantes.S_JOUR_DE_CONGE:
//                result = getHeureConge();
                System.err.println("Congé ---" + result);
                break;
            default:
                result = 0;
                break;
        }
        return result;
    }

    /*Appel de procedure standard*/
//    private double getHeureTravail(long idEmploye, Date debut, Date fin) {
//        return (Double) dao.getNbreHeureTravail(idEmploye, debut, fin, true,true);
//    }
//
//    private double getHeureConge(long idEmploye, Date debut) {
//        return (Double) dao.getNbreJourConge(idEmploye, debut);
//    }

      
}
