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

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Taxes implements Serializable {

    private long id;
    private String codeTaxe;
    private double taux;
    private String codeAppel;
    private String libellePrint;
    private boolean supp;
    private boolean actif = true;
    private String designation;
    private String module = "M";
    private Date dateSave = new Date();
    private double totalTaux, totalMontant;
    private Comptes compte = new Comptes();
    private List<ArticleTaxe> taxes;
    private boolean selectActif, new_, update;

    public Taxes() {
        taxes = new ArrayList<>();
    }

    public Taxes(long id) {
        this.id = id;
        taxes = new ArrayList<>();
    }

    public Taxes(String codeTaxe) {
        this.codeTaxe = codeTaxe;
        taxes = new ArrayList<>();
    }

    public String getLibellePrint() {
        return libellePrint != null ? libellePrint.trim().length() > 0 ? libellePrint : getCodeTaxe() : getCodeTaxe();
    }

    public void setLibellePrint(String libellePrint) {
        this.libellePrint = libellePrint;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public double getTotalMontant() {
        return totalMontant;
    }

    public void setTotalMontant(double totalMontant) {
        this.totalMontant = totalMontant;
    }

    public List<ArticleTaxe> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<ArticleTaxe> taxes) {
        this.taxes = taxes;
    }

    public String getModule() {
        return module != null ? (module.trim().length() > 0 ? module : "M") : "M";
    }

    public void setModule(String module) {
        this.module = module;
    }

    public double getTotalTaux() {
        return totalTaux;
    }

    public void setTotalTaux(double totalTaux) {
        this.totalTaux = totalTaux;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodeTaxe() {
        return codeTaxe != null ? codeTaxe : "";
    }

    public void setCodeTaxe(String codeTaxe) {
        this.codeTaxe = codeTaxe;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
        int hash = 7;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Taxes other = (Taxes) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
