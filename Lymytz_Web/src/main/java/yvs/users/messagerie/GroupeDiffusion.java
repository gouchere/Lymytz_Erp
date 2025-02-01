/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.societe.Societe;
import yvs.users.Users;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class GroupeDiffusion implements Serializable {

    private int id;
    private String libelle;
    private String reference;
    private List<DiffusionContact> diffusionContactList;
    private Users user = new Users();
    private boolean publics;
    private Societe societe = new Societe();
    private boolean selectActif;

    public GroupeDiffusion() {
        diffusionContactList = new ArrayList<>();
    }

    public GroupeDiffusion(int id) {
        this.id = id;
        diffusionContactList = new ArrayList<>();
    }

    public boolean isPublics() {
        return publics;
    }

    public void setPublics(boolean publics) {
        this.publics = publics;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Users getUser() {
        return user;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<DiffusionContact> getDiffusionContactList() {
        return diffusionContactList;
    }

    public void setDiffusionContactList(List<DiffusionContact> diffusionContactList) {
        this.diffusionContactList = diffusionContactList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.id;
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
        final GroupeDiffusion other = (GroupeDiffusion) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
