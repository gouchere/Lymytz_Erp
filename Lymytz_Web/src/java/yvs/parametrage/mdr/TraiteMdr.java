/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.mdr;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "traiteMdr")
@SessionScoped
public class TraiteMdr implements Serializable {

    private int id;
    private String natureTraite;
    private double pourcentage;
    private String modeDeReglement;
    private int duree;

    public TraiteMdr() {
    }

    public TraiteMdr(int id) {
        this.id = id;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModeDeReglement() {
        return modeDeReglement;
    }

    public void setModeDeReglement(String modeDeReglement) {
        this.modeDeReglement = modeDeReglement;
    }

    public String getNatureTraite() {
        return natureTraite;
    }

    public void setNatureTraite(String natureTraite) {
        this.natureTraite = natureTraite;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TraiteMdr other = (TraiteMdr) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
