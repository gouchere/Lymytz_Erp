/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.assurance;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class TypeAssurance implements Serializable {

    private Integer id;
    private String libelle;
    private String description;
    private Assureur assureur =  new Assureur();
    private List<Assurance> AssuranceList;
    private boolean selectActif;

    public TypeAssurance() {
        AssuranceList = new ArrayList<>();
    }

    public TypeAssurance(Integer id) {
        this.id = id;
        AssuranceList = new ArrayList<>();
    }

    public TypeAssurance(Integer id, String libelle) {
        this.id = id;
        this.libelle = libelle;
        AssuranceList = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Assureur getAssureur() {
        return assureur;
    }

    public void setAssureur(Assureur assureur) {
        this.assureur = assureur;
    }

    public List<Assurance> getAssuranceList() {
        return AssuranceList;
    }

    public void setAssuranceList(List<Assurance> AssuranceList) {
        this.AssuranceList = AssuranceList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final TypeAssurance other = (TypeAssurance) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
