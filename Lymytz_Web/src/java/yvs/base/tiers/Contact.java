/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Contact implements Serializable {

    private long id;
    private String numero;
    private boolean principal;
    private boolean selectActif, new_, update;

    public Contact() {
    }

    public Contact(long id) {
        this.id = id;
    }

    public Contact(String numero) {
        this.numero = numero;
    }

    public Contact(String numero, boolean principal) {
        this.numero = numero;
        this.principal = principal;
    }

    public Contact(long id, String numero, boolean principal) {
        this.id = id;
        this.numero = numero;
        this.principal = principal;
    }

    public boolean isUpdate() {
        return update;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Contact other = (Contact) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
