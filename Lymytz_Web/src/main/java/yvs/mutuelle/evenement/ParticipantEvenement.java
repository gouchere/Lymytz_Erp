/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.Mutualiste;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ParticipantEvenement implements Serializable {

    private long id;
    private Mutualiste mutualiste = new Mutualiste();
    private Activite activite = new Activite();
    private String roleMembre;
    private boolean organisateur;
    private boolean selectActif, new_;

    public ParticipantEvenement() {
    }

    public ParticipantEvenement(long id) {
        this.id = id;
    }

    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public String getRoleMembre() {
        return roleMembre;
    }

    public void setRoleMembre(String roleMembre) {
        this.roleMembre = roleMembre;
    }

    public boolean isOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(boolean organisateur) {
        this.organisateur = organisateur;
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

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ParticipantEvenement other = (ParticipantEvenement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
