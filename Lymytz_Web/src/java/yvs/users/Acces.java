/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Acces implements Serializable {

    private boolean multiSociete;
    private boolean multiAgence;
    private boolean superAdmin;

    public Acces() {
    }

    public boolean isMultiSociete() {
        return multiSociete;
    }

    public void setMultiSociete(boolean multiSociete) {
        this.multiSociete = multiSociete;
    }

    public boolean isMultiAgence() {
        return multiAgence;
    }

    public void setMultiAgence(boolean multiAgence) {
        this.multiAgence = multiAgence;
    }

    public boolean isSuperAdmin() {
        return superAdmin;
    }
 
    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }
   
    public void doNothing() {
//        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

}
