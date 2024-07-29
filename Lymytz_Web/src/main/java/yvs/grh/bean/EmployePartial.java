/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class EmployePartial implements Serializable {

    private long id;
    private String civilite;
    private String nom;
    private String prenom;
    private String nom_prenom;
    private String matricule;
    private String photos = "user1.png";
    private Date dateEmbauche = new Date();
    private Date dateNaissance = new Date();
    private Dictionnaire villeNaissance = new Dictionnaire();
    private Dictionnaire paysDorigine = new Dictionnaire();
    private String cni;
    private boolean error;

    public EmployePartial() {
    }

    public EmployePartial(long id) {
        this.id = id;
    }

    public EmployePartial(long id, String matricule) {
        this.id = id;
        this.matricule = matricule;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
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

    public String getNom_prenom() {
        nom_prenom = "";
        if (nom != null ? !nom.equals("") : false) {
            nom_prenom = nom;
        }
        if (prenom != null ? !prenom.equals("") : false) {
            nom_prenom += " " + prenom;
        }
        return nom_prenom;
    }

    public void setNom_prenom(String nom_prenom) {
        this.nom_prenom = nom_prenom;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Dictionnaire getVilleNaissance() {
        return villeNaissance;
    }

    public void setVilleNaissance(Dictionnaire villeNaissance) {
        this.villeNaissance = villeNaissance;
    }

    public Dictionnaire getPaysDorigine() {
        return paysDorigine;
    }

    public void setPaysDorigine(Dictionnaire paysDorigine) {
        this.paysDorigine = paysDorigine;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
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
        final EmployePartial other = (EmployePartial) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
