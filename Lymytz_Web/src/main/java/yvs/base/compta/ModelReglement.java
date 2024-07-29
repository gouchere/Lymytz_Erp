/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.base.YvsBaseTrancheReglement;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ModelReglement implements Serializable {

    private long id;
    private String designation;
    private String description;
    private char type = 'M';
    private Date dateSave = new Date();
    private String codeAcces;
    private List<YvsBaseTrancheReglement> tranches;
    private boolean selectActif, new_, update, actif = true, error, payeBeforeValide = false;

    public ModelReglement() {
        tranches = new ArrayList<>();
    }

    public ModelReglement(long id) {
        this.id = id;
        tranches = new ArrayList<>();
    }

    public ModelReglement(long id, String designation) {
        this.id = id;
        this.designation = designation;
        tranches = new ArrayList<>();
    }

    public ModelReglement(long id, String reference, String designation) {
        this.id = id;
        this.designation = designation;
        tranches = new ArrayList<>();
    }

    public char getType() {
        return String.valueOf(type) != null ? (String.valueOf(type).trim().length() > 0 ? type : 'M') : 'M';
    }

    public void setType(char type) {
        this.type = type;
    }

    public List<YvsBaseTrancheReglement> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsBaseTrancheReglement> tranches) {
        this.tranches = tranches;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(String codeAcces) {
        this.codeAcces = codeAcces;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isPayeBeforeValide() {
        return payeBeforeValide;
    }

    public void setPayeBeforeValide(boolean payeBeforeValide) {
        this.payeBeforeValide = payeBeforeValide;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ModelReglement other = (ModelReglement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
