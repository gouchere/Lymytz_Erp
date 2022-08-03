/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.base;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.users.Users;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class EmployeEquipe implements Serializable {

    private long id;
    private boolean actif, selectActif;
    private Users producteur = new Users();

    public EmployeEquipe() {
    }

    public EmployeEquipe(long id_) {
        this.id = id_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public long getId() {
        return id;
    }

    public Users getProducteur() {
        return producteur;
    }

    public void setProducteur(Users producteur) {
        this.producteur = producteur;
    }

    public void setId(long id) {
        this.id = id;
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
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final EmployeEquipe other = (EmployeEquipe) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
