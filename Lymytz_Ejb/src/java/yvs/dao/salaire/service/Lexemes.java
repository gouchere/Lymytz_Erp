/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

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
    public int typeElt;
    public double valeur;
    public String nom, expression, designation, code, expQuantite, expBase;
    public boolean systeme = false, categorie, contrat, employe, bulletin;
    public boolean quantite = false, expresion, pourcentage;
    public double tauxP, tauxS;
    public boolean retenu;
    private boolean regleConge, visible;
    private ExpressionFormule formule = new ExpressionFormule();

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

    public boolean isRegleConge() {
        return regleConge;
    }

    public void setRegleConge(boolean regleConge) {
        this.regleConge = regleConge;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public ExpressionFormule getFormule() {
        return formule;
    }

    public void setFormule(ExpressionFormule formule) {
        this.formule = formule;
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
