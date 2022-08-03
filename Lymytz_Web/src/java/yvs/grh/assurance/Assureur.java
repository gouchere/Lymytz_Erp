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
import yvs.parametrage.societe.Societe;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class Assureur implements Serializable {

    private Integer id;
    private String nom;
    private String adresse;
    private String telephone;
    private Societe societe = new Societe();
    private List<TypeAssurance> TypeAssuranceList;
    private boolean selectActif;

    public Assureur() {
        TypeAssuranceList = new ArrayList<>();
    }

    public Assureur(Integer id) {
        this.id = id;
        TypeAssuranceList = new ArrayList<>();
    }

    public Assureur(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
        TypeAssuranceList = new ArrayList<>();
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public List<TypeAssurance> getTypeAssuranceList() {
        return TypeAssuranceList;
    }

    public void setTypeAssuranceList(List<TypeAssurance> TypeAssuranceList) {
        this.TypeAssuranceList = TypeAssuranceList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final Assureur other = (Assureur) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
