/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.base;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class GrilleTaux implements Serializable {
    
    private long id;
    private double montantMinimal;
    private double montantMaximal;
    private double taux;
    private double periodeMaximal;
    private boolean selectActif, new_;
    
    public GrilleTaux() {
    }
    
    public GrilleTaux(long id) {
        this.id = id;
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
    
    public long getId() {
        return id;
    }
    
    public double getMontantMaximal() {
        return montantMaximal;
    }
    
    public void setMontantMaximal(double montantMaximal) {
        this.montantMaximal = montantMaximal;
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
    
    public double getTaux() {
        return taux;
    }
    
    public void setTaux(double taux) {
        this.taux = taux;
    }
    
    public double getPeriodeMaximal() {
        return periodeMaximal;
    }
    
    public void setPeriodeMaximal(double periodeMaximal) {
        this.periodeMaximal = periodeMaximal;
    }
    
//    public static GrilleTaux getDefault(double montant) {
//        GrilleTaux g = new GrilleTaux();
//        g.setId(0);
//        g.setMontantMaximal(montant);
//        g.setMontantMinimal(montant);
//        g.setPeriodeMaximal(10);
//        g.setTaux(10);
//        return g;
//    }
    
//    public static YvsMutGrilleTauxTypeCredit getDefault_(double montant) {
//        YvsMutGrilleTauxTypeCredit g = new YvsMutGrilleTauxTypeCredit();
//        g.setId((long)0);
//        g.setMontantMaximal(montant);
//        g.setMontantMinimal(montant);
//        g.setPeriodeMaximal((double)10);
//        g.setTaux(10.0);
//        return g;
//    }
    
    @Override
    public int hashCode() {
        int hash = 5;
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
        final GrilleTaux other = (GrilleTaux) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
