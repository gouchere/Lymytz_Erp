/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author Lymytz
 */
public class Banques {

    private int id;
    private String nom, codePostal, telephone;
    private boolean actif=true;
    private Dictionnaire ville = new Dictionnaire(), pays = new Dictionnaire();

    public Banques() {
    }

    public Banques(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Dictionnaire getVille() {
        return ville;
    }

    public void setVille(Dictionnaire ville) {
        this.ville = ville;
    }

    public Dictionnaire getPays() {
        return pays;
    }

    public void setPays(Dictionnaire pays) {
        this.pays = pays;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override

    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.id;
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
        final Banques other = (Banques) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
