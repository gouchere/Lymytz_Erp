/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.client;

import java.io.Serializable;
import java.util.Date;
import yvs.grh.bean.TypeCout;

/**
 *
 * @author Lymytz Dowes
 */
public class ElementAddContratsClient implements Serializable {

    private long id;
    private double montant;
    private char application = 'I';
    private int periodiciteApplication;
    private Date dateSave = new Date();
    private TypeCout typeCout = new TypeCout();
    private ContratsClient contrat = new ContratsClient();

    public ElementAddContratsClient() {
    }

    public ElementAddContratsClient(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public char getApplication() {
        return application;
    }

    public void setApplication(char application) {
        this.application = application;
    }

    public int getPeriodiciteApplication() {
        return periodiciteApplication;
    }

    public void setPeriodiciteApplication(int periodiciteApplication) {
        this.periodiciteApplication = periodiciteApplication;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public TypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(TypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public ContratsClient getContrat() {
        return contrat;
    }

    public void setContrat(ContratsClient contrat) {
        this.contrat = contrat;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ElementAddContratsClient other = (ElementAddContratsClient) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
