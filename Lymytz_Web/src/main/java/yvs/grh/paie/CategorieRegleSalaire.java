/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LYMYTZ-PC
 */
public class CategorieRegleSalaire implements Serializable {

    private long id;
    private String code, designation;
    private String description;
    private List<ElementSalaire> listeRegle;
    private boolean actif = true;
    private boolean defaultPrime;
    private boolean defaultretenue;

    public CategorieRegleSalaire() {
        listeRegle = new ArrayList<>();
    }

    public CategorieRegleSalaire(long id, String code) {
        this.id = id;
        this.code = code;
        listeRegle = new ArrayList<>();
    }

    public CategorieRegleSalaire(long id, String code, String designation) {
        this.id = id;
        this.code = code;
        this.designation = designation;
        listeRegle = new ArrayList<>();
    }

    public void setListeRegle(List<ElementSalaire> listeRegle) {
        this.listeRegle = listeRegle;
    }

    public List<ElementSalaire> getListeRegle() {
        return listeRegle;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final CategorieRegleSalaire other = (CategorieRegleSalaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CategorieRegleSalaire{" + "id=" + id + '}';
    }

    public boolean isDefaultPrime() {
        return defaultPrime;
    }

    public void setDefaultPrime(boolean defaultPrime) {
        this.defaultPrime = defaultPrime;
    }

    public boolean isDefaultretenue() {
        return defaultretenue;
    }

    public void setDefaultretenue(boolean defaultretenue) {
        this.defaultretenue = defaultretenue;
    }

}
