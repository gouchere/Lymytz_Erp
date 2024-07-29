/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.compta.YvsComptaContentModeleSaisi;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ModelesSasie implements Serializable {

    private long id;
    private String typeModele;
    private String intitule;
    private List<YvsComptaContentModeleSaisi> contenus;
    private boolean actif;

    public ModelesSasie() {
        contenus = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeModele() {
        return typeModele;
    }

    public void setTypeModele(String typeModele) {
        this.typeModele = typeModele;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public List<YvsComptaContentModeleSaisi> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComptaContentModeleSaisi> contenus) {
        this.contenus = contenus;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isActif() {
        return actif;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ModelesSasie other = (ModelesSasie) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
