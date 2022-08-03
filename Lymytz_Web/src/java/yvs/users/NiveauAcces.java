/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.societe.Societe;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class NiveauAcces implements Serializable {

    private long id;
    private String designation;
    private String description;
    private boolean supp;
    private boolean actif;
    private long grade;
    private boolean selectActif;
    private boolean superAdmin;
    private Date dateSave = new Date();
    private List<Users> usersList;
    private Societe societe = new Societe();

    public NiveauAcces() {
        usersList = new ArrayList<>();
    }

    public NiveauAcces(long id) {
        this();
        this.id = id;
    }

    public NiveauAcces(long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public long getId() {
        return id;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation != null ? designation : "";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public long getGrade() {
        return grade;
    }

    public void setGrade(long grade) {
        this.grade = grade;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final NiveauAcces other = (NiveauAcces) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
