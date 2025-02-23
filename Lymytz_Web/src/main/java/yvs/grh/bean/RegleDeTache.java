    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.taches.YvsGrhMontantTache;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class RegleDeTache implements Serializable {

    private long id;
    private String designation, description;
    private String code;
    private List<YvsGrhMontantTache> listeTache;
    private double montantTotal;
    private boolean actif = true;
    private boolean check = false;
    private boolean supp = false;
    private boolean selectActif;

    public RegleDeTache() {
        listeTache = new ArrayList<>();
    }

    public RegleDeTache(long id) {
        this.id = id;
    }

    public RegleDeTache(long id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isActif() {
        return actif;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public boolean isCheck() {
        return check;
    }

    public List<YvsGrhMontantTache> getListeTache() {
        return listeTache;
    }

    public void setListeTache(List<YvsGrhMontantTache> listeTache) {
        this.listeTache = listeTache;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean getActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final RegleDeTache other = (RegleDeTache) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RegleDeTache{" + "id=" + id + ", designation=" + designation + '}';
    }

}
