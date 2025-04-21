/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.base;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class TypeCrenaux implements Serializable, Comparable {

    private long id;
    private Date heureDebut, heureFin;
    private String reference, critere;
    private boolean actif;

    public TypeCrenaux() {
    }

    public TypeCrenaux(long id) {
        this.id = id;
    }

    public TypeCrenaux(long id, Date heureDebut, Date hereFin, String reference) {
        this.id = id;
        this.heureDebut = heureDebut;
        this.heureFin = hereFin;
        this.reference = reference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCritere() {
        return critere;
    }

    public void setCritere(String critere) {
        this.critere = critere;
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
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TypeCrenaux other = (TypeCrenaux) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        TypeCrenaux c = (TypeCrenaux) o;
        if (heureDebut.equals(c.heureDebut)) {
            if (heureFin.equals(c.heureFin)) {
                return Long.valueOf(id).compareTo(c.id);
            }
            return heureFin.compareTo(c.heureFin);
        }
        return heureDebut.compareTo(c.heureDebut);
    }
}
