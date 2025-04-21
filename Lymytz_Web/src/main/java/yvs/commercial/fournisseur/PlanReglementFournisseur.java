/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.fournisseur;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.ModelReglement;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PlanReglementFournisseur implements Serializable {

    private long id;
    private double montantMinimal;
    private double montantMaximal;
    private boolean actif;
    private ModeDeReglement model = new ModeDeReglement();
    private List<TrancheReglementFournisseur> tranches;
    private boolean selectActif, new_;

    public PlanReglementFournisseur() {
        tranches = new ArrayList<>();
    }

    public PlanReglementFournisseur(long id) {
        this.id = id;
        tranches = new ArrayList<>();
    }

    public List<TrancheReglementFournisseur> getTranches() {
        return tranches;
    }

    public void setTranches(List<TrancheReglementFournisseur> tranches) {
        this.tranches = tranches;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontantMinimal() {
        return montantMinimal;
    }

    public void setMontantMinimal(double montantMinimal) {
        this.montantMinimal = montantMinimal;
    }

    public double getMontantMaximal() {
        return montantMaximal;
    }

    public void setMontantMaximal(double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public ModeDeReglement getModel() {
        return model;
    }

    public void setModel(ModeDeReglement model) {
        this.model = model;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PlanReglementFournisseur other = (PlanReglementFournisseur) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
