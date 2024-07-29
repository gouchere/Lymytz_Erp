/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class FiltreMail implements Serializable {

    private int id;
    private String base = "E";
    private String valeur;
    private GroupeMessage groupeMessage = new GroupeMessage();

    public FiltreMail() {
    }

    public FiltreMail(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public GroupeMessage getGroupeMessage() {
        return groupeMessage;
    }

    public void setGroupeMessage(GroupeMessage groupeMessage) {
        this.groupeMessage = groupeMessage;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.id;
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
        final FiltreMail other = (FiltreMail) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
