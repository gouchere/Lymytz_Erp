/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.commercial.commission.YvsComPlanCommission;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class CategoriePerso implements Serializable {

    private long id;
    private String code;
    private String libelle;
    private String description;
    private Date dateSave = new Date();
    private CategoriePerso parent;
    private List<CategoriePerso> fils;
    private List<Personnel> personnels;
    private List<YvsComPlanCommission> commissions;
    private boolean selectActif, new_, update;

    public CategoriePerso() {
        fils = new ArrayList<>();
        personnels = new ArrayList<>();
        commissions = new ArrayList<>();
    }

    public CategoriePerso(long id) {
        this.id = id;
        fils = new ArrayList<>();
        personnels = new ArrayList<>();
        commissions = new ArrayList<>();
    }

    public CategoriePerso(long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
        fils = new ArrayList<>();
        personnels = new ArrayList<>();
        commissions = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CategoriePerso> getFils() {
        return fils;
    }

    public void setFils(List<CategoriePerso> fils) {
        this.fils = fils;
    }

    public CategoriePerso getParent() {
        return parent;
    }

    public void setParent(CategoriePerso parent) {
        this.parent = parent;
    }

    public List<Personnel> getPersonnels() {
        return personnels;
    }

    public void setPersonnels(List<Personnel> personnels) {
        this.personnels = personnels;
    }

    public List<YvsComPlanCommission> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<YvsComPlanCommission> commissions) {
        this.commissions = commissions;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final CategoriePerso other = (CategoriePerso) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
