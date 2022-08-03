/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ClassesStat implements Serializable {

    private long id;
    private String codeRef;
    private String designation;
    private boolean visibleJournal, visibleSynthese;
    private boolean actif;
    private long parent;
    private Date dateSave = new Date();

    public ClassesStat() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public boolean isVisibleJournal() {
        return visibleJournal;
    }

    public void setVisibleJournal(boolean visibleJournal) {
        this.visibleJournal = visibleJournal;
    }

    public boolean isVisibleSynthese() {
        return visibleSynthese;
    }

    public void setVisibleSynthese(boolean visibleSynthese) {
        this.visibleSynthese = visibleSynthese;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ClassesStat other = (ClassesStat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
