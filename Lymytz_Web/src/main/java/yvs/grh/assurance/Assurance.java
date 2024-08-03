/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.assurance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.Employe;
import yvs.grh.bean.PersonneEnCharge;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class Assurance implements Serializable {

    private Integer id;
    private String reference;
    private Double tauxCotisation;
    private Double tauxCouverture;
    private List<BilanAssurance> bilanAssuranceList;
    private TypeAssurance typeAssurance = new TypeAssurance();
    private List<Assurance> couvertureList;
    private PersonneEnCharge personneCharge = new PersonneEnCharge();
    private Employe employe = new Employe();
    private boolean selectActif;

    public Assurance() {
        bilanAssuranceList = new ArrayList<>();
        couvertureList = new ArrayList<>();
    }

    public Assurance(Integer id) {
        this.id = id;
        bilanAssuranceList = new ArrayList<>();
        couvertureList = new ArrayList<>();
    }

    public Assurance(Integer id, String reference) {
        this.id = id;
        this.reference = reference;
        bilanAssuranceList = new ArrayList<>();
        couvertureList = new ArrayList<>();
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public PersonneEnCharge getPersonneCharge() {
        return personneCharge;
    }

    public void setPersonneCharge(PersonneEnCharge personneCharge) {
        this.personneCharge = personneCharge;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getTauxCotisation() {
        return tauxCotisation;
    }

    public void setTauxCotisation(Double tauxCotisation) {
        this.tauxCotisation = tauxCotisation;
    }

    public Double getTauxCouverture() {
        return tauxCouverture;
    }

    public void setTauxCouverture(Double tauxCouverture) {
        this.tauxCouverture = tauxCouverture;
    }

    public List<BilanAssurance> getBilanAssuranceList() {
        return bilanAssuranceList;
    }

    public void setBilanAssuranceList(List<BilanAssurance> BilanAssuranceList) {
        this.bilanAssuranceList = BilanAssuranceList;
    }

    public TypeAssurance getTypeAssurance() {
        return typeAssurance;
    }

    public void setTypeAssurance(TypeAssurance typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

    public List<Assurance> getCouvertureList() {
        return couvertureList;
    }

    public void setCouvertureList(List<Assurance> AssuranceList) {
        this.couvertureList = AssuranceList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Assurance other = (Assurance) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
