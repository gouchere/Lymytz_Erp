/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import yvs.commercial.commission.PlanCommission;
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.users.Users;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Personnel implements Serializable {

    private long id;
    private Users users = new Users();
    private CategoriePerso categorie = new CategoriePerso();
    private PlanCommission planCommission = new PlanCommission();
    private boolean update, new_;

    public Personnel() {
    }

    public Personnel(long id) {
        this.id = id;
    }

    public boolean isUpdate() {
        return update;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public CategoriePerso getCategorie() {
        return categorie;
    }

    public void setCategorie(CategoriePerso categorie) {
        this.categorie = categorie;
    }

    public PlanCommission getPlanCommission() {
        return planCommission;
    }

    public void setPlanCommission(PlanCommission planCommission) {
        this.planCommission = planCommission;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Personnel other = (Personnel) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
