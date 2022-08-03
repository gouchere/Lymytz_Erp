/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.UniteMesure;
import yvs.parametrage.SectionDeValorisation;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class CentrePosteCharge implements Serializable {

    private int id;
    private boolean direct;
    private double valeur;
    private UniteMesure periode = new UniteMesure();
    private SectionDeValorisation centre = new SectionDeValorisation();
    private PosteCharge poste = new PosteCharge();
    private boolean selectActif;

    public CentrePosteCharge() {
    }

    public CentrePosteCharge(int id) {
        this.id = id;
    }

    public PosteCharge getPoste() {
        return poste;
    }

    public void setPoste(PosteCharge poste) {
        this.poste = poste;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public UniteMesure getPeriode() {
        return periode;
    }

    public void setPeriode(UniteMesure periode) {
        this.periode = periode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public SectionDeValorisation getCentre() {
        return centre;
    }

    public void setCentre(SectionDeValorisation centre) {
        this.centre = centre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.id;
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
        final CentrePosteCharge other = (CentrePosteCharge) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
