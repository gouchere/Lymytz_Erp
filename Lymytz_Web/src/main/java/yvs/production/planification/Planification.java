/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Planification implements Serializable {

    private long id;
    private String reference;
    private String type;    //PIC ou PDP
    private Date dateDebut, dateFin, datePlan;
    private int horizon = 1; //1 pour année - 2 pour mois - 3 pour semaine
    private String nameHorizon;
    private int periodicite = 1;
    private int amplitude = 1;
    private String periode; // M pour mois - S pour semaine - J pour jour
    private String namePeriode;
    private Date dateReference = new Date();
    private boolean selectActif;
    private short methode;  //methode d'ajustement
    private List<Planification> listRevision;

    public Planification() {
    }

    public Planification(long id) {
        this.id = id;
    }

    public String getNameHorizon() {
        return nameHorizon;
    }

    public void setNameHorizon(String nameHorizon) {
        this.nameHorizon = nameHorizon;
    }

    public String getNamePeriode() {
        return namePeriode;
    }

    public void setNamePeriode(String namePeriode) {
        this.namePeriode = namePeriode;
    }

    public Date getDatePlan() {
        return datePlan;
    }

    public void setDatePlan(Date datePlan) {
        this.datePlan = datePlan;
    }

    public int getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(int periodicite) {
        this.periodicite = periodicite;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude;
    }

    public int getHorizon() {
        return horizon;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Date getDateReference() {
        return dateReference;
    }

    public void setDateReference(Date dateReference) {
        this.dateReference = dateReference;
    }

    public short getMethode() {
        return methode;
    }

    public void setMethode(short methode) {
        this.methode = methode;
    }

    public void setListRevision(List<Planification> listRevision) {
        this.listRevision = listRevision;
    }

    public List<Planification> getListRevision() {
        return listRevision;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Planification other = (Planification) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
