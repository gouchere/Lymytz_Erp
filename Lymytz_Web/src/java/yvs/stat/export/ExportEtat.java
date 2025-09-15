/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import yvs.entity.stat.export.YvsStatExportColonne;

/**
 *
 * @author Lymytz Dowes
 */

@ManagedBean
@SessionScoped
@XmlRootElement(name = "etat")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportEtat implements Serializable {

    @XmlTransient 
    private int id;
    private String code;
    private String reference;
    @XmlTransient 
    private String fileName;
    @XmlTransient 
    private String format;
    @XmlTransient 
    private String separateur;
    @XmlTransient 
    private String formule;
    @XmlTransient 
    private char typeFormule;
    @XmlTransient 
    private Date dateSave = new Date();
    @XmlTransient 
    private List<YvsStatExportColonne> colonnes;
    @XmlElementWrapper(name = "valeurs")
    @XmlElement(name = "valeur")
    private List<ExportColonne> colonnesBean;

    public ExportEtat() {
        colonnes = new ArrayList<>();
        colonnesBean = new ArrayList<>();
    }

    public ExportEtat(int id) {
        this();
        this.id = id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public List<ExportColonne> getColonnesBean() {
        return colonnesBean;
    }

    public void setColonnesBean(List<ExportColonne> colonnesBean) {
        this.colonnesBean = colonnesBean;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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
