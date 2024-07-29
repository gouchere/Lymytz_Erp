/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

/**
 *
 * @author hp Elite 8300
 */
public class AffectationAnalytique {

    private long id;
    private Comptes comptes=new Comptes();
    private CentreAnalytique centreAnal=new CentreAnalytique();
    private double coefficient;

    public AffectationAnalytique() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Comptes getComptes() {
        return comptes;
    }

    public void setComptes(Comptes comptes) {
        this.comptes = comptes;
    }

    public CentreAnalytique getCentreAnal() {
        return centreAnal;
    }

    public void setCentreAnal(CentreAnalytique centreAnal) {
        this.centreAnal = centreAnal;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

}
