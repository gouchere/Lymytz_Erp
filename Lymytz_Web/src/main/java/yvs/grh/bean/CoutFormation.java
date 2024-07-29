/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class CoutFormation implements Serializable {

    private TypeCout typeCout;
    private double montant;
    private FormationEmps formationEmp = new FormationEmps();

    public CoutFormation() {
        typeCout = new TypeCout();
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

    public FormationEmps getFormationEmp() {
        return formationEmp;
    }

    public void setFormationEmp(FormationEmps formationEmp) {
        this.formationEmp = formationEmp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.typeCout);
        hash = 67 * hash + Objects.hashCode(this.formationEmp);
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
        final CoutFormation other = (CoutFormation) obj;
        if (!Objects.equals(this.typeCout, other.typeCout)) {
            return false;
        }
        if (!Objects.equals(this.formationEmp, other.formationEmp)) {
            return false;
        }
        return true;
    }
    
}
