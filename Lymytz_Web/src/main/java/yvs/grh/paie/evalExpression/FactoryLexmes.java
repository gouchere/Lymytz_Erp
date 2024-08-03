/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie.evalExpression;

/**
 *
 * @author LYMYTZ-PC
 *
 * cette classe est une fabrique de lexèmes. elle contient les méthodes qui
 * traite une chaine de formule de façon à pouvoir les évaluer
 */
public class FactoryLexmes {

    String texte;    //représente la chaine d'expression à décomposer en lexèmes
    int suivant = 0;//permet de parcourir la chaine de texte
    char c = ' ';//caractère courant
//le paramètre de ce constructeur est la chaine d'expression représentant la formule

    public FactoryLexmes(String texte) {
        this.texte = texte;
        c = ' ';
        suivant = 0;
    }

    /**
     * @param code, le code de l'expression en paramètre sert à éviter les
     * formule récursive
     * @return
     */
    public Lexemes suivant(String code) {
        sauterLesBlancs();
        if (Character.isLetter(c)) {
            String mot = ident();
//evite les formule récursive
            if (code != null) {
                if (code.equals(mot)) {
                    return null;    /// ici c'est une erreur fatal qui doit interrumpre toute l'opération
                }
            }
//vérifie si mot n'est pas un mot clé. si ce n'est pas un mot clé alors on le considère comme une variable
            if (motCle(mot)) {
                return new Lexemes(mot, true);
            } else {
                return new Lexemes(mot, false);
            }

        } else if (Character.isDigit(c)) {
            return new Lexemes(nombre());
        } else {
            switch (c) {
                case UniteLexicale.L_PLUS:
                case UniteLexicale.L_MOINS:
                case UniteLexicale.L_FOIS:
                case UniteLexicale.L_DIV:
                case UniteLexicale.L_PARG:
                case UniteLexicale.L_PARD:
                case UniteLexicale.L_SUP:
                case UniteLexicale.L_INF:
                case UniteLexicale.L_EQUALS:
                case UniteLexicale.L_ET:
                case UniteLexicale.L_OU:
                    Lexemes e = new Lexemes(c);
                    avancer();
                    return e;
                default:
                    return null;
            }
        }

    }

    String ident() {
        StringBuffer r = new StringBuffer("");
        while (Character.isLetterOrDigit(c) || String.valueOf(c).equals("_")) {
            r = r.append(c);
            avancer();
            if (c == '.') {
                r.append(c);
                avancer();
            }
        }
        if (r.toString().equals("ABS")) {
            r.append(decompose());
        }
        return new String(r);
    }

    void avancer() {
        if (suivant < texte.length()) {
            c = texte.charAt(suivant);
            suivant++;
        } else {
            c = UniteLexicale.EOF;
        }
    }

    double nombre() {
        int r = 0;
        double d = 0;
        int cpt = 1;
        boolean decimal = false;
        while (Character.isDigit(c)) {
            if (!decimal) {
                r = r * 10 + c - '0';
            } else {
                d = d + Math.pow(10, -cpt) * Integer.valueOf(String.valueOf(c));
                cpt++;
            }
            avancer();
            if (c == '.') {
                decimal = true;
                avancer();
            }
        }
        return r + d;
    }

    boolean motCle(String str) {
        return UtilsTest.tabMotCle.containsKey(str);
    }

    void sauterLesBlancs() {
        while (Character.isWhitespace(c)) {
            avancer();
        }
    }

    private String decompose() {
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
}
