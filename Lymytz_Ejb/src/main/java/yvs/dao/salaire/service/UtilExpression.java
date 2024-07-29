 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LYMYTZ
 */
public final class UtilExpression {

    public List<ExpressionConditionel> tab;
    char c = ' ';
    int pos = 0;
    String chaine;

    public UtilExpression(String ch) {
        tab = new ArrayList<>();
        chaine = ch;
    }

    //utile seulement pour l'extraction et la séparation des chaines conditionnelles
    public ExpressionConditionel decompose() {
        sauterLesBlancs();
        String mot = indent();
        switch (mot) {
            case "SI":
            case "SINONSI": {
                //récupère la chaine qui suis délimité par "(" et ")"
                ExpressionConditionel re = new ExpressionConditionel();
                re.setCondition(giveCondition());
                re.setExpression(giveExpression());
                re.setConditionOk(true);
//            tab.add(giveCondition());   //condition
//            tab.add(giveExpression());  //expression
                tab.add(re);
                decompose();
                break;
            }
            case "SINON": {
                ExpressionConditionel re = new ExpressionConditionel();
                re.setCondition("");
                re.setExpression(giveExpression());
                re.setConditionOk(true);
                tab.add(re);
                decompose();
                break;
            }
        }
        return null;
    }

    //récupère un mot cohérent de la chaine de phrase qu'on est entrain de traiter. i.e. un mot commençant par une lettre et ne contenant que des lettre ou des chiffres
    String indent() {
        StringBuffer r = new StringBuffer("");
        while (Character.isLetterOrDigit(c)) {
            r = r.append(c);
            avancer();
        }
        return r.toString();
    }
//cette méthode permet d'avancer d'un caractère dans la chaine de mot qu'on veux extraire

    private void avancer() {
        if (pos < chaine.length()) {
            c = chaine.charAt(pos);
            pos++;
        } else {
            c = '#';
        }
    }

    //récupère la condition
    public String giveCondition() {
        sauterLesBlancs();
        StringBuffer condition = new StringBuffer("");
        int countC = 0;
        while (c != '#') {
            if (c == '(') {
                countC++;
            }
            if (c == ')') {
                countC--;
            }
            condition = condition.append(c);
            avancer();
            if (countC == 0) {
                return condition.toString();
            }
        }
        return new String(condition);
    }

    //récupère l'expression de la condition
    String giveExpression() {
        sauterLesBlancs();
        StringBuffer condition = new StringBuffer("");
        int countC = 0;
        while (c != '#') {
            if (c == '{') {
                countC++;
            }
            if (c == '}') {
                countC--;
            }
            condition = condition.append(c);
            avancer();
            if (countC == 0) {
                String s = condition.toString().trim();
                if (s.length() > 1) {
                    //en enlevant le premier et le dernier caractère qui correspondent en effet aux caractères "{" et "}"
                    return s.substring(1, s.length() - 1);
                }
            }
        }
        return new String(condition);
    }

    public void sauterLesBlancs() {
        while (Character.isWhitespace(c)) {
            avancer();
        }
    }

    private boolean paire(int n) {
        return (n % 2 == 0);
    }

    public static String extract(String ch, String motif) {
        return ch.substring(motif.length());
    }    

}
