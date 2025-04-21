/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import yvs.mutuelle.base.Poste;
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PosteEmploye implements Serializable {

    private long id;
    private Date dateOccupation = new Date();
    private boolean actif;
    private boolean selectActif;
    private Poste poste = new Poste();

    public PosteEmploye() {
    }

    public PosteEmploye(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateOccupation() {
        return dateOccupation;
    }

    public void setDateOccupation(Date dateOccupation) {
        this.dateOccupation = dateOccupation;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
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
        final PosteEmploye other = (PosteEmploye) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
