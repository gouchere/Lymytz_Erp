/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.presence.TrancheHoraire;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class PlanningWork implements Serializable {

    private long id;
    private Date jour=new Date(), dateFin;
    private Employe employe = new Employe();
    private TrancheHoraire tranche = new TrancheHoraire();
    private List<PlanningTravail> listPlanningTravail;
    private boolean actif=true;

    public PlanningWork() {
        listPlanningTravail = new ArrayList<>();
    }

    public PlanningWork(Date jour) {
        this.jour = jour;
        listPlanningTravail = new ArrayList<>();
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Date getJour() {
        return jour;
    }

    public void setJour(Date jour) {
        this.jour = jour;
    }

    public List<PlanningTravail> getListPlanningTravail() {
        return listPlanningTravail;
    }

    public void setListPlanningTravail(List<PlanningTravail> listPlanningTravail) {
        this.listPlanningTravail = listPlanningTravail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.jour);
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
        final PlanningWork other = (PlanningWork) obj;
        if (!Objects.equals(this.jour, other.jour)) {
            return false;
        }
        return true;
    }

}
