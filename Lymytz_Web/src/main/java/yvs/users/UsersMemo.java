/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class UsersMemo implements Serializable {

    private long id;
    private String titre;
    private String description;
    private Date dateMemo = new Date();
    private Date dateDebutRappel = new Date();
    private Date dateFinRappel = new Date();
    private int dureeRappel;
    private Users users = new Users();

    public UsersMemo() {
    }

    public UsersMemo(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateMemo() {
        return dateMemo;
    }

    public void setDateMemo(Date dateMemo) {
        this.dateMemo = dateMemo;
    }

    public Date getDateDebutRappel() {
        return dateDebutRappel;
    }

    public void setDateDebutRappel(Date dateDebutRappel) {
        this.dateDebutRappel = dateDebutRappel;
    }

    public int getDureeRappel() {
        return dureeRappel;
    }

    public void setDureeRappel(int dureeRappel) {
        this.dureeRappel = dureeRappel;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Date getDateFinRappel() {
        return dateFinRappel;
    }

    public void setDateFinRappel(Date dateFinRappel) {
        this.dateFinRappel = dateFinRappel;
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
        final UsersMemo other = (UsersMemo) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
