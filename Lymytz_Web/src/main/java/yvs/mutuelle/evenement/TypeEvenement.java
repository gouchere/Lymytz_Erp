/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.evenement.YvsMutTauxContribution;
import yvs.mutuelle.Mutuelle;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class TypeEvenement implements Serializable {

    private long id;
    private String designation;
    private String description;
    private int nombreRepresentant;
    private String nombreParticipant;
    private boolean lierMutualiste;
    private Mutuelle mutuelle = new Mutuelle();
    private Date dateSave= new Date();
    private List<YvsMutTauxContribution> contributions;
    private boolean selectActif, new_;

    public TypeEvenement() {
        contributions = new ArrayList<>();
    }

    public TypeEvenement(long id) {
        this.id = id;
        contributions = new ArrayList<>();
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getNombreParticipant() {
        return nombreParticipant;
    }

    public void setNombreParticipant(String nombreParticipant) {
        this.nombreParticipant = nombreParticipant;
    }

    public List<YvsMutTauxContribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<YvsMutTauxContribution> contributions) {
        this.contributions = contributions;
    }

    public boolean isLierMutualiste() {
        return lierMutualiste;
    }

    public void setLierMutualiste(boolean lierMutualiste) {
        this.lierMutualiste = lierMutualiste;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNombreRepresentant() {
        return nombreRepresentant;
    }

    public void setNombreRepresentant(int nombreRepresentant) {
        this.nombreRepresentant = nombreRepresentant;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TypeEvenement other = (TypeEvenement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
