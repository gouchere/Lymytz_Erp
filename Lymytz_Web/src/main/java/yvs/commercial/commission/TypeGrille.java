/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.commission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.commercial.commission.YvsComTrancheTaux;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class TypeGrille implements Serializable {

    private int id;
    private String reference;
    private char cible;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private List<YvsComTrancheTaux> tranches;

    public TypeGrille() {
        tranches = new ArrayList<>();
    }

    public TypeGrille(int id) {
        this();
        this.id = id;
    }

    public TypeGrille(int id, String reference) {
        this(id);
        this.reference = reference;
    }

    public TypeGrille(int id, String reference, char cible) {
        this(id, reference);
        this.cible = cible;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public char getCible() {
        return Character.valueOf(cible) != null ? String.valueOf(cible).trim().length() > 0 ? cible : 'C' : 'C';
    }

    public void setCible(char cible) {
        this.cible = cible;
    }

    public List<YvsComTrancheTaux> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComTrancheTaux> tranches) {
        this.tranches = tranches;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
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
        final TypeGrille other = (TypeGrille) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
