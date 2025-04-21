/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.util.ArrayList;
import java.util.List;
import yvs.entity.produits.group.YvsBorneTranches;
import yvs.entity.produits.group.YvsTranches;

/**
 *
 * @author GOUCHERE YVES
 */
public class TrancheVal {

    private int id;
    private String reference;
    private String modelTranche; //indique le model tarifaire(valeur ou quantité)
    private List<YvsBorneTranches> listBorne;
    private boolean actif;

    public TrancheVal() {
        listBorne = new ArrayList<>();
    }

    public TrancheVal(int id, String reference, String modelTranche) {
        this.id = id;
        this.reference = reference;
        this.modelTranche = modelTranche;
    }

    public TrancheVal(int id, String modelTranche) {
        this.id = id;
        this.modelTranche = modelTranche;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelTranche() {
        return modelTranche;
    }

    public void setModelTranche(String modelTranche) {
        this.modelTranche = modelTranche;
    }

    public List<YvsBorneTranches> getListBorne() {
        return listBorne;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setListBorne(List<YvsBorneTranches> listBorne) {
        this.listBorne = listBorne;
    }

    public YvsTranches buildTranche(TrancheVal t) {
        YvsTranches tr = new YvsTranches(t.getId());
        tr.setModelTranche(t.getModelTranche());
        return tr;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrancheVal other = (TrancheVal) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        return hash;
    }
}
