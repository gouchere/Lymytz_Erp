/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean.mission;

import java.io.Serializable;
import java.util.Date;
import yvs.grh.bean.TypeCout;

/**
 *
 * @author LYMYTZ-PC
 */
public class DetailFraisMission implements Serializable {

    private long id;
    private TypeCout typeCout = new TypeCout();
    private double montant;
    private boolean proportionelDuree;   //indique si le détail de frais doit être multiplié par la durée de la mission
    private boolean save; //permet d'indiquer si la ligne est déjà persisté ou non
    private Date dateSave = new Date();

    public DetailFraisMission() {
        proportionelDuree = true;
    }

    public DetailFraisMission(double montant) {
        this.montant = montant;
    }

    public TypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(TypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isProportionelDuree() {
        return proportionelDuree;
    }

    public void setProportionelDuree(boolean proportionelDuree) {
        this.proportionelDuree = proportionelDuree;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DetailFraisMission other = (DetailFraisMission) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
