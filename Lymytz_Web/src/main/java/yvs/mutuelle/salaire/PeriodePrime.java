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
public class PeriodePrime {

    private long id;
    private Date debut, fin;
    private boolean paye;

    public PeriodePrime() {
    }

    public PeriodePrime(long id, Date debut, Date fin, boolean paye) {
        this.id = id;
        this.debut = debut;
        this.fin = fin;
        this.paye = paye;
    }

    public PeriodePrime(Date debut, Date fin, boolean paye) {
        this.debut = debut;
        this.fin = fin;
        this.paye = paye;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setPaye(boolean paye) {
        this.paye = paye;
    }

    public boolean isPaye() {
        return paye;
    }

}
