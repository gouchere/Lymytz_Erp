/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.util.Date;

/**
 *
 * @author hp Elite 8300
 */
public class OrdreCalculSalaire {

    private long id;
    private Date dateJour = new Date();
    private Date dateExecution = new Date();
    private Date heureExecution = new Date();
    private Date debutMois = new Date();
    private Date finMois = new Date();
    private boolean realise;
    private String reference;
    private boolean cloture;    //indique si une période à déjà été cloturé

    public OrdreCalculSalaire() {
    }

    public OrdreCalculSalaire(long id) {
        this.id = id;    
    }

    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateJour() {
        return dateJour;
    }

    public void setDateJour(Date dateJour) {
        this.dateJour = dateJour;
    }

    public Date getDateExecution() {
        return dateExecution;
    }

    public void setDateExecution(Date dateExecution) {
        this.dateExecution = dateExecution;
    }

    public Date getHeureExecution() {
        return heureExecution;
    }

    public void setHeureExecution(Date heureExecution) {
        this.heureExecution = heureExecution;
    }

    public boolean isRealise() {
        return realise;
    }

    public void setRealise(boolean realise) {
        this.realise = realise;
    }

    public Date getDebutMois() {
        return debutMois;
    }

    public void setDebutMois(Date debutMois) {
        this.debutMois = debutMois;
    }

    public Date getFinMois() {
        return finMois;
    }

    public void setFinMois(Date finMois) {
        this.finMois = finMois;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isCloture() {
        return cloture;
    }

    public void setCloture(boolean cloture) {
        this.cloture = cloture;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final OrdreCalculSalaire other = (OrdreCalculSalaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
