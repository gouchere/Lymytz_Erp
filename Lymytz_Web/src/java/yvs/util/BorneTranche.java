/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.util.Comparator;
import java.util.Date;
import yvs.entity.produits.group.YvsBorneTranches;
import yvs.entity.produits.group.YvsTranches;

/**
 *
 * @author GOUCHERE YVES
 */
public class BorneTranche implements Comparator<BorneTranche> {

    private long id;
    private double borne;
    private double remise;
    private double prix;
    private TrancheVal tranche = new TrancheVal();
    private Date dateSave = new Date();

    public BorneTranche() {
    }

    public BorneTranche(long id) {
        this.id = id;
    }

    public double getBorne() {
        return borne;
    }

    public void setBorne(double borne) {
        this.borne = borne;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TrancheVal getTranche() {
        return tranche;
    }

    public void setTranche(TrancheVal tranche) {
        this.tranche = tranche;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsBorneTranches buildBorne(BorneTranche b) {
        YvsBorneTranches bb = new YvsBorneTranches(b.getId());
        bb.setBorne(b.getBorne());
        bb.setPrix(b.getPrix());
        bb.setRemise(b.getRemise());
        return bb;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BorneTranche other = (BorneTranche) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public int compare(BorneTranche o1, BorneTranche o2) {
        if (o1.getBorne() < o2.getBorne()) {
            return -1;
        } else if (o1.getBorne() > o2.getBorne()) {
            return 1;
        } else {
            return 0;
        }
    }
}
