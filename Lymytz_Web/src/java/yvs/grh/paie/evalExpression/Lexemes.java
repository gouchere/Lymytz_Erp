/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie.evalExpression;

import java.util.Objects;

/**
 *
 * @author LYMYTZ cette classe permet d'extraire une suite de lexème d'une
 * chaine de texte représentant une expression à évaluer le lexème à vocation de
 * structurer une expression à évaluer
 */
public class Lexemes {

    private long idElt;
    public int nature;  //permet de connaitre la nature d'un élément (définit par les constantes de la classe UniteLexicale)
    public int typeElt; // Valeur Fixe, Pourcentage, Quantité, Expression algébrique
    public double valeur;
    /*nom de la règle*/
    public String nom;
    /*code de la règle*/
    public String code;
    /*si la règle est une expression, on écrit là la chaine correspondant à l'expression*/
    public String expression;
    /*si la règle est un pourcentage ou une quantité, on écrit là la chaine correspondant à l'expression de base du pourcentage et la chaine correspondant à la quantité*/
    public String expQuantite, expBase;
    public String designation;
    public boolean systeme = false, categorie, contrat, employe, bulletin;
    public boolean quantite = false, expresion, pourcentage;
    public double tauxP, tauxS;
    public boolean retenu;
    private boolean regleConge, visible;
    
    private ExpressionConditionel formule = new ExpressionConditionel();

    public Lexemes() {
    }

    public Lexemes(double i) {
        this.nature = UniteLexicale.NOMBRE;
        this.nom = "" + i;
        this.valeur = i;
    }

    //instancie un Lexème identifiant ou mot clé
    public Lexemes(String nom, boolean mc) {
        if (!mc) {
            this.nature = UniteLexicale.ID_VAR;
            this.code = nom;
        } else {
            this.nature = UniteLexicale.MOT_CLE;
        }
        this.nom = nom;
    }

    public Lexemes(char op) {
        nom = String.valueOf(op);
        nature = op;// nature étant un entier, il récupère le code ASCCI du caractère représentant l'opérateur
    }

    public long getIdElt() {
        return idElt;
    }

    public void setIdElt(long idElt) {
        this.idElt = idElt;
    }

    public void setRegleConge(boolean regleConge) {
        this.regleConge = regleConge;
    }

    public boolean isRegleConge() {
        return regleConge;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setFormule(ExpressionConditionel formule) {
        this.formule = formule;
    }

    public ExpressionConditionel getFormule() {
        return formule;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.nom);
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
        final Lexemes other = (Lexemes) obj;
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Lexemes{" + "nature=" + nature + ", valeur=" + valeur + ", nom=" + nom + ", expression=" + expression + ", code=" + code + '}';
    }

}
