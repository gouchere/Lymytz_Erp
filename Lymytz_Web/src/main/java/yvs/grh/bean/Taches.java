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
import yvs.entity.grh.taches.YvsGrhTacheEmps;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class Taches implements Serializable {

    private long id;
    private String codeTache;
    private String description;
    private boolean supp = false;
    private boolean attribuer, selectActif;
    private String designation;
    private boolean actif = true;
    private double montant;
    private PrimeTache primeTache;
    private RegleDeTache regleTache;
    private List<YvsGrhTacheEmps> employes;

    public Taches() {
        regleTache = new RegleDeTache();
        primeTache = new PrimeTache();
        employes = new ArrayList<>();
    }

    public Taches(long id) {
        this.id = id;
    }

    public Taches(long id, String codeTache, boolean actif) {
        this.id = id;
        this.codeTache = codeTache;
        this.actif = actif;
    }

    public RegleDeTache getRegleTache() {
        return regleTache;
    }

    public void setRegleTache(RegleDeTache regleTache) {
        this.regleTache = regleTache;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isAttribuer() {
        return attribuer;
    }

    public void setAttribuer(boolean attribuer) {
        this.attribuer = attribuer;
    }

    public PrimeTache getPrimeTache() {
        return primeTache;
    }

    public void setPrimeTache(PrimeTache primeTache) {
        this.primeTache = primeTache;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCodeTache() {
        return codeTache;
    }

    public void setCodeTache(String codeTache) {
        this.codeTache = codeTache;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<YvsGrhTacheEmps> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhTacheEmps> employes) {
        this.employes = employes;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Taches other = (Taches) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
