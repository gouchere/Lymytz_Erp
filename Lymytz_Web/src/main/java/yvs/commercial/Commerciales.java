/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.tiers.Tiers;
import yvs.commercial.commission.PlanCommission;
import yvs.entity.commercial.commission.YvsComCommissionCommerciaux;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.users.Users;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Commerciales implements Serializable {

    private long id;
    private String code;
    private String nom;
    private String prenom;
    private String nomPrenom;
    private String telephone;
    private boolean error, list, actif, defaut;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Users user = new Users();
    private Tiers tiers = new Tiers();
    private PlanCommission commission = new PlanCommission();
    private double sommeCommission;
    private List<YvsComCommercialVente> factures;

    public Commerciales() {
        factures = new ArrayList<>();
    }

    public Commerciales(long id) {
        this();
        this.id = id;
    }

    public Commerciales(long id, String nomPrenom) {
        this(id);
        this.nomPrenom = nomPrenom;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public PlanCommission getCommission() {
        return commission;
    }

    public void setCommission(PlanCommission commission) {
        this.commission = commission;
    }

    public List<YvsComCommercialVente> getFactures() {
        return factures;
    }

    public void setFactures(List<YvsComCommercialVente> factures) {
        this.factures = factures;
    }

    public double getSommeCommission() {
        return sommeCommission;
    }

    public void setSommeCommission(double sommeCommission) {
        this.sommeCommission = sommeCommission;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Commerciales other = (Commerciales) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
