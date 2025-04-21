package yvs.commercial.objectifs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.Commerciales;
import yvs.commercial.depot.PointVente;
import yvs.parametrage.agence.Agence;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ObjectifsCom implements Serializable {

    private String source = "C";
    private ModelObjectif model = new ModelObjectif();
    private Commerciales comercial = new Commerciales();
    private Agence agence = new Agence();
    private PointVente pointVente = new PointVente();
    private PeriodesObjectifs periode = new PeriodesObjectifs();
    private double montantObjectif;

    public ObjectifsCom() {
    }

    public String getSource() {
        return source != null ? source.trim().length() > 0 ? source : "C" : "C";
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ModelObjectif getModel() {
        return model;
    }

    public void setModel(ModelObjectif model) {
        this.model = model;
    }

    public Commerciales getComercial() {
        return comercial;
    }

    public void setComercial(Commerciales comercial) {
        this.comercial = comercial;
    }

    public double getMontantObjectif() {
        return montantObjectif;
    }

    public void setMontantObjectif(double montantObjectif) {
        this.montantObjectif = montantObjectif;
    }

    public PeriodesObjectifs getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodesObjectifs periode) {
        this.periode = periode;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public PointVente getPointVente() {
        return pointVente;
    }

    public void setPointVente(PointVente pointVente) {
        this.pointVente = pointVente;
    }

}
