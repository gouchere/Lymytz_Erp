/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.ElementAdditionnel;
import yvs.parametrage.PlanPrelevement;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class PrelevementEmps implements Serializable {

    private long id;
    private Date datePrelevement = new Date();
    private double valeur;
    private ElementAdditionnel elementAdd = new ElementAdditionnel();
    private PlanPrelevement plan = new PlanPrelevement();
    private boolean suspendu;
    private char statut;
    private String reference;

    public PrelevementEmps() {
    }

    public PrelevementEmps(long id) {
        this.id = id;
    }

    public void setElementAdd(ElementAdditionnel elementAdd) {
        this.elementAdd = elementAdd;
    }

    public ElementAdditionnel getElementAdd() {
        return elementAdd;
    }

    public void setSuspendu(boolean suspendu) {
        this.suspendu = suspendu;
    }

    public boolean isSuspendu() {
        return suspendu;
    }

    public PlanPrelevement getPlan() {
        return plan;
    }

    public void setPlan(PlanPrelevement plan) {
        this.plan = plan;
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

    public Date getDatePrelevement() {
        return datePrelevement;
    }

    public void setDatePrelevement(Date datePrelevement) {
        this.datePrelevement = datePrelevement;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.id);
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
        final PrelevementEmps other = (PrelevementEmps) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
