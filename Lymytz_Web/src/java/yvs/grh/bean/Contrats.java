/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Contrats implements Serializable{

    private long id;
    private String type; //CDD, CDI, Temporaire
    private Date debut = new Date();
    private Date fin = new Date();
    private Date finEssai = new Date();
    private double salaireHoraire, salaireMensuel;
    private RegleDeTache regleTache = new RegleDeTache();
    private String reference;
    private String file;
//    private Employe employe = new Employe();

    public Contrats() {
    }

    public Contrats(long id) {
        this.id = id;
    }

    public Contrats(long id, String reference) {
        this.id = id;
        this.reference = reference;
    }

    public void setRegleTache(RegleDeTache regleTache) {
        this.regleTache = regleTache;
    }

    public RegleDeTache getRegleTache() {
        return regleTache;
    }
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(double salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public double getSalaireMensuel() {
        return salaireMensuel;
    }

    public void setSalaireMensuel(double salaireMensuel) {
        this.salaireMensuel = salaireMensuel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public Date getFinEssai() {
        return finEssai;
    }

    public void setFinEssai(Date finEssai) {
        this.finEssai = finEssai;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Contrats other = (Contrats) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
