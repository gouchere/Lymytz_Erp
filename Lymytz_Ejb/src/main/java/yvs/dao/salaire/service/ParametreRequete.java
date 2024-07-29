/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author hp Elite 8300 Explicitation de la propriété typeClause Dans une
 * requete, on peut utiliser la clause or dans 3 contexte 1- sur les valeur 2-
 * Sur les attribut de la requete (X=x OR Y=x OR Z=x) 2- Sur les attribut et les
 * valeur (X=x OR Y=y)
 */
public class ParametreRequete {

    private String attribut;    //indique l'attribut sur lequel on veux filtrer
    private String paramNome;  //indique le paramètre nomé
    private Object objet;       //valeur à comparer
    private Object otherObjet;
    private String predicat;    //AND ou OR
    private String operation;   //=, BETWEEN, != IN
    private boolean clauseOr;
    private int typeClauseOr; // 1-Sur l'attribue  2-Sur la valeur 3-Sur une suite de valeur
    private List<ParametreRequete> otherExpression;
    private boolean lastParam = false;

    public ParametreRequete() {
        otherExpression = new ArrayList<>();
    }

    public ParametreRequete(String fieldParam) {
        this.paramNome = fieldParam;
        otherExpression = new ArrayList<>();
    }

    public ParametreRequete(String attribut, String fielParam, Object valeur) {
        this.attribut = attribut;
        this.objet = valeur;
        this.paramNome = fielParam;
        otherExpression = new ArrayList<>();
    }

    public ParametreRequete(String attribut, String fielParam, Object valeur, String operation, String predicat) {
        this.attribut = attribut;
        this.objet = valeur;
        this.paramNome = fielParam;
        this.operation = operation;
        this.predicat = predicat;
        otherExpression = new ArrayList<>();
    }

    public ParametreRequete(String attribut, String fielParam, Object valeur, Object otherObjet, String operation, String predicat) {
        this.attribut = attribut;
        this.paramNome = fielParam;
        this.objet = valeur;
        this.otherObjet = otherObjet;
        this.predicat = predicat;
        this.operation = operation;
        otherExpression = new ArrayList<>();
    }

    public List<ParametreRequete> getOtherExpression() {
        return otherExpression;
    }

    public void setOtherExpression(List<ParametreRequete> otherExpression) {
        this.otherExpression = otherExpression;
    }

    public String getAttribut() {
        
        return attribut;
    }

    public void setAttribut(String attribut) {
        this.attribut = attribut;
    }

    public Object getObjet() {
        return objet;
    }

    public void setObjet(Object objet) {
        this.objet = objet;
    }

    public String getPredicat() {
        return predicat != null ? predicat.trim().length() > 0 ? predicat : "AND" : "AND";
    }

    public void setPredicat(String predicat) {
        this.predicat = predicat;
    }

    public Object getOtherObjet() {
        return otherObjet;
    }

    public String getOperation() {
        return operation != null ? operation.trim().length() > 0 ? operation : "=" : "=";
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setOtherObjet(Object otherObjet) {
        this.otherObjet = otherObjet;
    }

    public String getParamNome() {
        return paramNome;
    }

    public void setParamNome(String paramNome) {
        this.paramNome = paramNome;
    }

    public boolean isClauseOr() {
        return clauseOr;
    }

    public void setClauseOr(boolean clauseOr) {
        this.clauseOr = clauseOr;
    }

    public int getTypeClauseOr() {
        return typeClauseOr;
    }

    public void setTypeClauseOr(int typeClauseOr) {
        this.typeClauseOr = typeClauseOr;
    }

    public void setLastParam(boolean lastParam) {
        this.lastParam = lastParam;
    }

    public boolean isLastParam() {
        return lastParam;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.paramNome);
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
        final ParametreRequete other = (ParametreRequete) obj;
        if (!Objects.equals(this.paramNome, other.paramNome)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return objet.toString();
    }

    
}
