/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author hp Elite 8300 epermet de présenter le recapitulatif des congés par
 * mois
 */
public class RecapConges {

    private Date month = new Date();
    private List<Conges> listConge;
    private Date numberHour;
    private int nbreJour = 0;

    public RecapConges() {
        listConge = new ArrayList<>();
    }

    public RecapConges(Date month) {
        listConge = new ArrayList<>();
        this.month = month;
    }

    public List<Conges> getListConge() {
        return listConge;
    }

    public void setListConge(List<Conges> listConge) {
        this.listConge = listConge;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public Date getNumberHour() {
        return numberHour;
    }

    public void setNumberHour(Date numberHour) {
        this.numberHour = numberHour;
    }

    public int getNbreJour() {
        return nbreJour;
    }

    public void setNbreJour(int nbreJour) {
        this.nbreJour = nbreJour;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.month);
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
        final RecapConges other = (RecapConges) obj;
        if (!Objects.equals(this.month, other.month)) {
            return false;
        }
        return true;
    }

}
