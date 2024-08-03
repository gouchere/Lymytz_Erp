/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.stat.export.YvsStatExportColonne;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ExportEtat implements Serializable {

    private int id;
    private String code;
    private String libelle;
    private String fileName;
    private String format;
    private String reference;
    private String separateur;
    private String tablePrincipal;
    private String colonnePrincipal;
    private String orderBy;
    private String formule;
    private char typeFormule;
    private boolean withOrdreBy;
    private boolean forExportation = true;
    private List<YvsStatExportColonne> colonnes;

    public ExportEtat() {
        colonnes = new ArrayList<>();
    }

    public ExportEtat(int id) {
        this.id = id;
        colonnes = new ArrayList<>();
    }

    public boolean isWithOrdreBy() {
        return orderBy != null ? orderBy.trim().length() > 0 : false;
    }

    public void setWithOrdreBy(boolean withOrdreBy) {
        this.withOrdreBy = withOrdreBy;
    }

    public char getTypeFormule() {
        return String.valueOf(typeFormule) != null ? String.valueOf(typeFormule).trim().length() > 0 ? typeFormule : 'S' : 'S';
    }

    public void setTypeFormule(char typeFormule) {
        this.typeFormule = typeFormule;
    }

    public String getFormule() {
        return formule;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isForExportation() {
        return forExportation;
    }

    public void setForExportation(boolean forExportation) {
        this.forExportation = forExportation;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getReference() {
        return reference != null ? reference : "";
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSeparateur() {
        return separateur != null ? separateur.trim().length() > 0 ? separateur : ";" : ";";
    }

    public void setSeparateur(String separateur) {
        this.separateur = separateur;
    }

    public String getTablePrincipal() {
        return tablePrincipal;
    }

    public void setTablePrincipal(String tablePrincipal) {
        this.tablePrincipal = tablePrincipal;
    }

    public String getColonnePrincipal() {
        return colonnePrincipal;
    }

    public void setColonnePrincipal(String colonnePrincipal) {
        this.colonnePrincipal = colonnePrincipal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<YvsStatExportColonne> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<YvsStatExportColonne> colonnes) {
        this.colonnes = colonnes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
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
        final ExportEtat other = (ExportEtat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
