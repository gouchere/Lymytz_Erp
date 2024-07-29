/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class CompteBancaire implements Serializable {

    private Integer id;
    private String numeroCompte, codeBanque, cleCompte;
    private Boolean supp;
    private Boolean actif;
    private Banques banque = new Banques();

    public CompteBancaire() {
    }

    public Banques getBanque() {
        return banque;
    }

    public void setBanque(Banques banque) {
        this.banque = banque;
    }

    public CompteBancaire(Integer id) {
        this.id = id;
    }

    public CompteBancaire(Integer id, String numeroCompte) {
        this.id = id;
        this.numeroCompte = numeroCompte;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean isSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getCleCompte() {
        return cleCompte;
    }

    public void setCleCompte(String cleCompte) {
        this.cleCompte = cleCompte;
    }

    public void setCodeBanque(String codeBanque) {
        this.codeBanque = codeBanque;
    }

    public String getCodeBanque() {
        return codeBanque;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final CompteBancaire other = (CompteBancaire) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
