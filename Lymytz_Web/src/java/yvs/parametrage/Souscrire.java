/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Lymytz-pc
 */
public class Souscrire implements Serializable {

    private long id;
    private String nomSociete;
    private String telephone;
    private String email;
    private String pays;
    private String ville;

    private String nomUsers = "ADMINISTRATEUR";
    private String identifiant = "ADMIN";
    private String password = "ADMIN";
    
    private long localSociete;

    private List<String> modules;

    public Souscrire() {
    }

    public Souscrire(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getNomUsers() {
        return nomUsers;
    }

    public void setNomUsers(String nomUsers) {
        this.nomUsers = nomUsers;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public long getLocalSociete() {
        return localSociete;
    }

    public void setLocalSociete(long localSociete) {
        this.localSociete = localSociete;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Souscrire other = (Souscrire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Souscrire{" + "nomSociete=" + nomSociete + '}';
    }

}
