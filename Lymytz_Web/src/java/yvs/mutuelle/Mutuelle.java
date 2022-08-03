/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutParametre;
import yvs.entity.param.YvsAgences;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Mutuelle implements Serializable {

    private long id;
    private String designation, code;
    private String logo;
    private Date dateCreation, dateSave = new Date();
    private double montantEpargne;
    private double montantInscription;
    private double montantAssurance;
    private List<YvsAgences> agences;
    private List<YvsMutCaisse> caisses;
    private YvsMutParametre parametres;
    private boolean selectActif, new_;

    public Mutuelle() {
        caisses = new ArrayList<>();
        agences = new ArrayList<>();
    }

    public Mutuelle(long id) {
        this.id = id;
        caisses = new ArrayList<>();
        agences = new ArrayList<>();
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsMutParametre getParametres() {
        return parametres;
    }

    public void setParametres(YvsMutParametre parametres) {
        this.parametres = parametres;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getMontantInscription() {
        return montantInscription;
    }

    public void setMontantInscription(double montantInscription) {
        this.montantInscription = montantInscription;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public long getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public double getMontantEpargne() {
        return montantEpargne;
    }

    public void setMontantEpargne(double montantEpargne) {
        this.montantEpargne = montantEpargne;
    }

    public List<YvsAgences> getAgences() {
        return agences;
    }

    public void setAgences(List<YvsAgences> agences) {
        this.agences = agences;
    }

    public List<YvsMutCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsMutCaisse> caisses) {
        this.caisses = caisses;
    }

    public double getMontantAssurance() {
        return montantAssurance;
    }

    public void setMontantAssurance(double montantAssurance) {
        this.montantAssurance = montantAssurance;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Mutuelle other = (Mutuelle) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
