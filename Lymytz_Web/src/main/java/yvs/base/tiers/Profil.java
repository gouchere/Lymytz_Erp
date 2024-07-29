/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.compta.YvsBasePlanComptable;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Profil implements Serializable {

    private long id;
    private long idTiers;
    private long numero; //numéro de la ligne
    private String code;
    private String nom;
    private String prenom;
    private String type;
    private String tableTiers;
    private String nomPrenom;
    private boolean actif;
    private String matricule;
    private YvsBasePlanComptable compte = new YvsBasePlanComptable();
    private String value;

    public Profil() {

    }

    public Profil(long id, String type) {
        this.id = id;
        this.type = type;
        this.value = id + "-" + type;
    }

    public Profil(long id, String code, String nom, String prenom, String type, boolean actif) {
        this(id, code, nom, prenom, new YvsBasePlanComptable(), type, actif);
    }

    public Profil(long id, String code, String nom, String prenom, YvsBasePlanComptable compte, String type, boolean actif) {
        this(id, type);
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.compte = compte;
        this.actif = actif;
    }

    public Profil(long id, String code, String nom, String prenom, YvsBasePlanComptable compte, String type, boolean actif, long numero, long idTiers, String table) {
        this(id, code, nom, prenom, compte, type, actif);
        this.numero = numero;
        this.idTiers = idTiers;
        this.tableTiers = table;
    }

    public Profil(long id, String code, String nom, String prenom, YvsBasePlanComptable compte, String type, boolean actif, long numero, long idTiers, String table, String matricule) {
        this(id, code, nom, prenom, compte, type, actif);
        this.numero = numero;
        this.idTiers = idTiers;
        this.tableTiers = table;
        this.matricule = matricule;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public long getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(long idTiers) {
        this.idTiers = idTiers;
    }

    public String getTableTiers() {
        return tableTiers;
    }

    public void setTableTiers(String tableTiers) {
        this.tableTiers = tableTiers;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPrenom() {
        nomPrenom = "";
        if (!(getNom() == null || getNom().trim().equals(""))) {
            nomPrenom = getNom();
        }
        if (!(getPrenom() == null || getPrenom().trim().equals(""))) {
            if (nomPrenom == null || nomPrenom.trim().equals("")) {
                nomPrenom = getPrenom();
            } else {
                nomPrenom += " " + getPrenom();
            }
        }
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 53 * hash + Objects.hashCode(this.type);
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
        final Profil other = (Profil) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

}
