/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat.export;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ExportColonne implements Serializable {

    private int id;
    private int ordre;
    private String colonne;
    private String libelle;
    private boolean visible;
    private boolean integrer;
    private String tableName;
    private boolean contrainte, realContrainte;
    private boolean colonneDate;
    private String formatDate;
    private String defautValeur;
    private String colonneLiee;
    private Character orderBy;
    private char sensContrainte;
    private String tableNameLiee;
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

    public String getFormatDate() {
       return formatDate != null ? formatDate.trim().length() > 0 ? formatDate : "dd-MM-yyyy" : "dd-MM-yyyy";
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public boolean isColonneDate() {
        return colonneDate;
    }

    public void setColonneDate(boolean colonneDate) {
        this.colonneDate = colonneDate;
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

    public ExportEtat getEtat() {
        return etat;
    }

    public void setEtat(ExportEtat etat) {
        this.etat = etat;
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
