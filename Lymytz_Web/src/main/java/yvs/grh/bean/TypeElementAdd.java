/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Comptes;

/**
 *
 * @author LYMYTZ spécificie le type des éléments Additionnel (prime et
 * retenues)
 */
@ManagedBean
@SessionScoped
public class TypeElementAdd extends TypeContrat implements Serializable {

    private boolean retenue;
    private String description;
    private boolean visibleEnGescom;
    private Comptes compte = new Comptes();

    public TypeElementAdd() {

    }

    public TypeElementAdd(long id) {
        super(id);
    }

    public boolean isRetenue() {
        return retenue;
    }

    public void setRetenue(boolean retenue) {
        this.retenue = retenue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVisibleEnGescom(boolean visibleEnGescom) {
        this.visibleEnGescom = visibleEnGescom;
    }

    public boolean isVisibleEnGescom() {
        return visibleEnGescom;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

}
