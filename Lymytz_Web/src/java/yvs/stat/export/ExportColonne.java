/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat.export;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Lymytz Dowes
 */
@XmlAccessorType(XmlAccessType.FIELD)
@ManagedBean
@SessionScoped
public class ExportColonne implements Serializable {

    @XmlTransient
    private int id;    
    @XmlTransient
    private int ordre;
    private String colonne;
    @XmlTransient
    private String libelle;
    private String type;
    private boolean key;
    private boolean visible;
    private boolean integrer;
    private String tableName;
    private boolean contrainte;    
    @XmlTransient
    private boolean realContrainte;
    @XmlTransient
    private String defautValeur;
    private String colonneLiee;
    @XmlTransient
    private Character orderBy;
    @XmlTransient
    private char sensContrainte;
    private String tableNameLiee;
    @XmlTransient
    private String jointure;
    private boolean condition;
    private String conditionExpression, conditionExpressionOther;
    private String conditionOperator;
    private Object valeur;
    @XmlTransient
    private Date dateSave = new Date();
    @XmlTransient
    private ExportEtat etat = new ExportEtat();

    public ExportColonne() {
    }

    public ExportColonne(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefautValeur() {
        return defautValeur;
    }

    public void setDefautValeur(String defautValeur) {
        this.defautValeur = defautValeur;
    }

    public char getSensContrainte() {
        return String.valueOf(sensContrainte) != null ? String.valueOf(sensContrainte).trim().length() > 0 ? sensContrainte : 'N' : 'N';
    }

    public void setSensContrainte(char sensContrainte) {
        this.sensContrainte = sensContrainte;
    }

    public Character getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Character orderBy) {
        this.orderBy = orderBy;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public String getType() {
        return type != null ? !type.trim().isEmpty() ? type : "character varying" : "character varying";
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isColonneFormater() {
        return getType().equals("date");
    }

    public String getColonne() {
        return colonne;
    }

    public void setColonne(String colonne) {
        this.colonne = colonne;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isIntegrer() {
        return integrer;
    }

    public void setIntegrer(boolean integrer) {
        this.integrer = integrer;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isRealContrainte() {
        return realContrainte;
    }

    public void setRealContrainte(boolean realContrainte) {
        this.realContrainte = realContrainte;
    }

    public boolean isContrainte() {
        return contrainte;
    }

    public void setContrainte(boolean contrainte) {
        this.contrainte = contrainte;
    }

    public String getColonneLiee() {
        return colonneLiee;
    }

    public void setColonneLiee(String colonneLiee) {
        this.colonneLiee = colonneLiee;
    }

    public String getTableNameLiee() {
        return tableNameLiee;
    }

    public void setTableNameLiee(String tableNameLiee) {
        this.tableNameLiee = tableNameLiee;
    }

    public String getJointure() {
        return jointure != null ? jointure.trim().length() > 0 ? jointure : "LEFT JOIN" : "LEFT JOIN";
    }

    public void setJointure(String jointure) {
        this.jointure = jointure;
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public String getConditionExpressionOther() {
        return conditionExpressionOther;
    }

    public void setConditionExpressionOther(String conditionExpressionOther) {
        this.conditionExpressionOther = conditionExpressionOther;
    }

    public String getConditionOperator() {
        return conditionOperator != null ? conditionOperator.trim().length() > 0 ? conditionOperator : "=" : "=";
    }

    public void setConditionOperator(String conditionOperator) {
        this.conditionOperator = conditionOperator;
    }

    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    public ExportEtat getEtat() {
        return etat;
    }

    public void setEtat(ExportEtat etat) {
        this.etat = etat;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.id;
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
        final ExportColonne other = (ExportColonne) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
