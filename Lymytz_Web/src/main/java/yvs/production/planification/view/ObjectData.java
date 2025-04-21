/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author hp Elite 8300
 */
public class ObjectData implements Serializable {

    private long id;
    private double value;
    private double montantBase;
    private String id_;
    private List<CoeficientPlanif> listCoeficient;

    public ObjectData() {
        listCoeficient = new ArrayList<>();
    }

    public ObjectData(double value, String id) {
        listCoeficient = new ArrayList<>();
        this.value = value;
        this.id_ = id;
    }

    public ObjectData(long id, double value, double montantBase, String id_) {
        this.id = id;
        this.value = value;
        this.montantBase = montantBase;
        this.id_ = id_;
        listCoeficient = new ArrayList<>();
    }

    public ObjectData(double value) {
        this.value = value;
        listCoeficient = new ArrayList<>();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<CoeficientPlanif> getListCoeficient() {
        return listCoeficient;
    }

    public void setListCoeficient(List<CoeficientPlanif> listCoeficient) {
        this.listCoeficient = listCoeficient;
    }

    public double getMontantBase() {
        return montantBase;
    }

    public void setMontantBase(double montantBase) {
        this.montantBase = montantBase;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id_);
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
        final ObjectData other = (ObjectData) obj;
        if (!Objects.equals(this.id_, other.id_)) {
            return false;
        }
        return true;
    }

}
