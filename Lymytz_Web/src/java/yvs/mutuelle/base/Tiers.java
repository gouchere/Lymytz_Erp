/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.base;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.Employe;
import yvs.mutuelle.Mutuelle;
import yvs.parametrage.societe.Societe;

/**
 *
 * @author lymytz
 */
@ManagedBean(name = "tiersMut")
@SessionScoped
public class Tiers implements Serializable {

    private long id;
    private String nom;
    private String prenom;
    private String raisonSociale;
    private String adresse;
    private String telephone;
    private String typeTiers;//Externe -- Employe -- Societe
    private Mutuelle mutuelle = new Mutuelle();
    private boolean actif;

    private long idExterne;
    private Employe employe = new Employe();
    private Societe societe = new Societe();
    private Date dateSave = new Date();
    private boolean selectActif, new_;

    public Tiers() {
    }

    public Tiers(long id) {
        this.id = id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTypeTiers() {
        return typeTiers;
    }

    public void setTypeTiers(String typeTiers) {
        this.typeTiers = typeTiers;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(long idExterne) {
        this.idExterne = idExterne;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isActif() {
        return actif;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getNom_Prenom() {
        return (nom != null) ? ((prenom != null) ? nom.concat(" ").concat(prenom) : nom) : (prenom != null) ? prenom : "";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Tiers other = (Tiers) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
