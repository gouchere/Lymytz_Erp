/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.YvsMutDefaultUseFor;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class CaisseMutuelle implements Serializable {

    private long id;
    private String reference;
    private double solde;
    private boolean selectActif, new_, actif;
    private String proprietaire;
    private boolean principale;
    private Date dateSave = new Date();
    private Mutualiste responsable = new Mutualiste();
    private Mutuelle mutuelle = new Mutuelle();
    private List<YvsMutDefaultUseFor> defaultActivites;

    public CaisseMutuelle() {
        defaultActivites = new ArrayList<>();
    }

    public CaisseMutuelle(long id) {
        this.id = id;
        defaultActivites = new ArrayList<>();
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Mutualiste getResponsable() {
        return responsable;
    }

    public void setResponsable(Mutualiste responsable) {
        this.responsable = responsable;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<YvsMutDefaultUseFor> getDefaultActivites() {
        return defaultActivites;
    }

    public void setDefaultActivites(List<YvsMutDefaultUseFor> defaultActivites) {
        this.defaultActivites = defaultActivites;
    }

    public void setPrincipale(boolean principale) {
        this.principale = principale;
    }

    public boolean isPrincipale() {
        return principale;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final CaisseMutuelle other = (CaisseMutuelle) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
