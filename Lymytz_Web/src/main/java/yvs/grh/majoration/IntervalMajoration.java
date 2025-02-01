/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.majoration;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class IntervalMajoration implements Serializable {

    private Long id;
    private int valeurMinimal;
    private int valeurMaximal;
    private double nbreJour;
    private boolean taux;
    private boolean actif;
    private MajorationConge majorationConge = new MajorationConge();

    public IntervalMajoration() {
    }

    public IntervalMajoration(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public boolean isTaux() {
        return taux;
    }

    public void setTaux(boolean taux) {
        this.taux = taux;
    }


    public int getValeurMinimal() {
        return valeurMinimal;
    }

    public void setValeurMinimal(int valeurMinimal) {
        this.valeurMinimal = valeurMinimal;
    }

    public int getValeurMaximal() {
        return valeurMaximal;
    }

    public void setValeurMaximal(int valeurMaximal) {
        this.valeurMaximal = valeurMaximal;
    }

    public double getNbreJour() {
        return nbreJour;
    }

    public void setNbreJour(double nbreJour) {
        this.nbreJour = nbreJour;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public MajorationConge getMajorationConge() {
        return majorationConge;
    }

    public void setMajorationConge(MajorationConge majorationConge) {
        this.majorationConge = majorationConge;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final IntervalMajoration other = (IntervalMajoration) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
